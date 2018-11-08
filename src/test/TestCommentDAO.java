import dao.CommentDAO;
import dao.impl.CommentDAOImpl;
import db.ConnectionManager;
import dto.CommentDTO;
import entites.Comment;
import entites.Pagination;
import org.junit.Assert;
import org.junit.Test;

import java.sql.*;
import java.util.Date;
import java.util.List;


public class TestCommentDAO {
    @Test
    public void fullTest() throws SQLException {
        CommentDAO commentDAO = CommentDAOImpl.getInstance();
        Connection cn = ConnectionManager.getConnection();
        cn.setAutoCommit(false);

        try {
            long beforeSaving = getAll("COMMENTS", cn);
            long numberOfCommentsInThePostBeforeSaving = commentDAO.getNumberOfCommentsInThePost(1);
            Comment comment = commentDAO.save(
                    new Comment(1, 1, "Test comment", new Timestamp(new Date().getTime())));
            long afterSaving = getAll("comments", cn);

            System.out.println(beforeSaving + " " + afterSaving);
            Assert.assertNotEquals(beforeSaving, afterSaving);

            long numberOfCommentsInThePostAfterSaving = commentDAO.getNumberOfCommentsInThePost(1);
            Assert.assertNotEquals(numberOfCommentsInThePostBeforeSaving, numberOfCommentsInThePostAfterSaving);

            Comment commentReceivedFromDB = commentDAO.get(comment.getId());
            Assert.assertEquals(comment, commentReceivedFromDB);

            commentReceivedFromDB.setText("Test UPDATED comment");
            commentDAO.update(commentReceivedFromDB);
            commentReceivedFromDB = commentDAO.get(comment.getId());
            Assert.assertNotEquals(comment, commentReceivedFromDB);

            List<CommentDTO> ListWithNoComments =
                    commentDAO.getCommentsInThePostWithOffsetAndLimit(1, new Pagination(1000, 1000));
            Assert.assertTrue(ListWithNoComments.size() == 0);

            List<CommentDTO> listOfCommentsBeforeDeletion =
                    commentDAO.getCommentsInThePostWithOffsetAndLimit(1, new Pagination(0, 1000));
            int deletedNumber = commentDAO.delete(comment.getId());
            long afterDeletion = getAll("COMMENTS", cn);
            List<CommentDTO> listOfCommentsAfterDeletion =
                    commentDAO.getCommentsInThePostWithOffsetAndLimit(1, new Pagination(0, 1000));
            Assert.assertNotEquals(listOfCommentsBeforeDeletion.size(), listOfCommentsAfterDeletion.size());
            Assert.assertEquals(beforeSaving, afterDeletion);
            Assert.assertEquals(beforeSaving, afterSaving - deletedNumber);

            cn.commit();
        } catch (Exception | Error e) {
            cn.rollback();
            throw e;
        }
    }

    private long getAll(String tableName, Connection cn) throws SQLException {
        String getAll = "SELECT Count(*) FROM " + tableName;
        PreparedStatement psGetAll = cn.prepareStatement(getAll);
        ResultSet rs = psGetAll.executeQuery();
        rs.next();

        return rs.getLong(1);
    }
}
