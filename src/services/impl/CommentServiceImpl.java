package services.impl;

import dao.CommentDAO;
import dao.impl.CommentDAOImpl;
import dto.CommentDTO;
import entites.Comment;
import entites.Pagination;
import services.CommentService;
import services.ServiceException;

import java.sql.SQLException;
import java.util.List;

public class CommentServiceImpl extends AbstractService implements CommentService {
    private static volatile CommentService INSTANCE = null;
    private CommentDAO commentDAO = CommentDAOImpl.getInstance();

    private CommentServiceImpl() {
    }

    @Override
    public Comment save(Comment comment) {
        try {
            startTransaction();
            comment = commentDAO.save(comment);
            commit();
        } catch (SQLException e) {
            rollback();
            throw new ServiceException("Error creating Comment " + comment);
        }

        return comment;
    }

    @Override
    public Comment get(long id) {
        Comment comment;

        try {
            startTransaction();
            comment = commentDAO.get(id);
            commit();
        } catch (SQLException e) {
            rollback();
            throw new ServiceException("Error getting Comment by id " + id);
        }

        return comment;
    }

    @Override
    public void update(Comment comment) {
        try {
            startTransaction();
            commentDAO.update(comment);
            commit();
        } catch (SQLException e) {
            rollback();
            throw new ServiceException("Error updating Comment " + comment);
        }
    }

    @Override
    public int delete(long id) {
        int numberOfDeleted;

        try {
            startTransaction();
            numberOfDeleted = commentDAO.delete(id);
            commit();
        } catch (SQLException e) {
            rollback();
            throw new ServiceException("Error deleting Comment by id " + id);
        }

        return numberOfDeleted;
    }

    @Override
    public List<CommentDTO> getCommentsInThePostWithOffsetAndLimit(long postID, Pagination pagination) {
        List<CommentDTO> list;

        try {
            startTransaction();
            list = commentDAO.getCommentsInThePostWithOffsetAndLimit(postID, pagination);
            commit();
        } catch (SQLException e) {
            rollback();
            StringBuilder sb = new StringBuilder();
            sb.append("Error getting Comments In The Post With Offset And Limit by ID ")
                    .append(postID)
                    .append(" and Pagination")
                    .append(pagination);

            throw new ServiceException(sb.toString());
        }

        return list;
    }

    @Override
    public long getNumberOfCommentsInThePost(long postID) {
        long numberOfCommentsInThePost;

        try {
            startTransaction();
            numberOfCommentsInThePost = commentDAO.getNumberOfCommentsInThePost(postID);
            commit();
        } catch (SQLException e) {
            rollback();
            throw new ServiceException("Error getting Number Of Comments In The Post by id " + postID);
        }

        return numberOfCommentsInThePost;
    }

    public static CommentService getInstance() {
        CommentService commentService = INSTANCE;

        if (commentService == null) {
            synchronized (CommentServiceImpl.class) {

                commentService = INSTANCE;

                if (commentService == null) {
                    INSTANCE = commentService = new CommentServiceImpl();
                }
            }
        }

        return commentService;
    }
}
