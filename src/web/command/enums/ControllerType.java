package web.command.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import web.command.Controller;
import web.command.impl.*;

@Getter
@AllArgsConstructor
public enum  ControllerType {
    //frontController
    CHAT_MESSAGES("chat_messages", new ChatMessagesController()),
    COMMENTS_LENGTH("comments_length", new CommentsLengthController()),
    CREATE_COMMENT("create_comment", new CreateCommentController()),
    COMMENT_LIST("comment_list", new CommentsListController()),
    CREATE_POST("create_post", new CreatePostController()),
    CREATE_USER("create_user", new CreateUserController()),
    LIKE_UNLIKE_POST("like_unlike_post", new LikeUnlikePostController()),
    LOGIN("login", new LoginController()),
    LOGOUT("logout", new LogoutController()),
    MESSAGE_LIST("message_list", new MessageListController()),
    POST_LENGTH("post_length", new PostLengthController()),
    POST_LIST("post_list", new PostListController()),
    POST_SINGLE("post_single", new PostSingleController()),
    USER_AVATAR("user_avatar", new UserAvatarController()),
    USER_NAME("user_name", new UserNameController()),
    GET_LOCALE("get_locale", new LocaleController()),
    CHECK_LOCAL_STORAGE("check_local_storage", new CheckLocalStorageController()),
    CHANGE_USER_STATUS("change_status", new ChangeUserStatusController()),
    DELETE_POST("delete_post", new DeletePostController()),
    USER_ROLE_AND_STATUS("user_role_and_status", new UserRoleAndStatusController()),
    DOWNLOAD_HISTORY_MESSAGE("download_history_message", new DownloadMessageHistoryController()),
    CHANGE_USER_ROLE("change_role", new ChangeUserRoleController()),
    DELETE_COMMENT("delete_comment", new DeleteCommentController());

    private String pageName;
    private Controller controller;

    public static ControllerType getByPageName(String page) {
        for (ControllerType type : ControllerType.values()) {
            if (type.pageName.equalsIgnoreCase(page)) {
                return type;
            }
        }

        throw new RuntimeException("!!!ОШИБКА ЗАПРОСА!!!");
    }
}
