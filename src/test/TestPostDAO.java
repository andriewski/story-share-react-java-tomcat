import dao.PostDAO;
import dao.impl.PostDAOImpl;
import db.ConnectionManager;
import dto.PostDTO;
import entites.Pagination;
import entites.Post;
import org.junit.Assert;
import org.junit.Test;
import java.util.Date;
import java.sql.*;
import java.util.List;


public class TestPostDAO {
    @Test
    public void fullTest() throws SQLException {
        PostDAO postDAO = PostDAOImpl.getInstance();
        Connection cn = ConnectionManager.getConnection();
        cn.setAutoCommit(false);

        try {
            long beforeAdding = getAll("POSTS", cn);
            long allPosts = postDAO.getNumberOfAllPosts();
            System.out.println(allPosts);
            Assert.assertEquals(beforeAdding, allPosts);

            Post post = postDAO.save(new Post(
                    "Test1!", new Timestamp(new Date().getTime()), 1, "TestPicture"));
            long afterAdding = getAll("POSTS", cn);
            System.out.println(beforeAdding + " " + afterAdding);
            Assert.assertNotEquals(beforeAdding, afterAdding);

            Post postReceivedFromDB = postDAO.get(post.getId());
            Assert.assertEquals(post, postReceivedFromDB);

            postReceivedFromDB.setText("Test1 after Update");
            postDAO.update(postReceivedFromDB);

            Post postUpdatedPost = postDAO.get(post.getId());
            Assert.assertNotEquals(post, postUpdatedPost);

            List<PostDTO> listOfPosts = postDAO.getPostsWithPagination(
                    new Pagination(0, 1000), 1);
            System.out.println(listOfPosts.size());
            for (PostDTO listOfPost : listOfPosts) {
                System.out.println(listOfPost);
            }
            Assert.assertEquals(afterAdding, listOfPosts.size());

            postDAO.likePostByUser(post.getId(), 1);
            PostDTO likedPost = postDAO.getSinglePostDTO(1, post.getId());
            Assert.assertTrue(likedPost.isLiked());
            System.out.println(likedPost);

            postDAO.unlikePostByUser(post.getId(), 1);
            PostDTO unlikedPost = postDAO.getSinglePostDTO(1, post.getId());
            System.out.println(unlikedPost);
            Assert.assertFalse(unlikedPost.isLiked());

            int deletedNumber = postDAO.delete(post.getId());
            List<PostDTO> listOfPostsAfterDeletion = postDAO.getPostsWithPagination(
                    new Pagination(0, 1000), 1);
            long afterDeletion = getAll("POSTS", cn);
            Assert.assertEquals(beforeAdding, listOfPostsAfterDeletion.size());
            Assert.assertEquals(beforeAdding, afterDeletion);
            Assert.assertEquals(beforeAdding, afterAdding - deletedNumber);

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