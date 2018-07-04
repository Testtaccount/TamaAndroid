package com.tama.q_municate_db.managers;


import android.os.Bundle;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.quickblox.q_municate_user_service.model.QMUser;
import com.quickblox.q_municate_user_service.model.QMUserColumns;
import com.tama.q_municate_db.managers.base.BaseManager;
import com.tama.q_municate_db.models.Dialog;
import com.tama.q_municate_db.models.DialogOccupant;
import com.tama.q_municate_db.utils.ErrorUtils;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class UserDataManager extends BaseManager<QMUser> {

    private static final String TAG = UserDataManager.class.getSimpleName();

    private Dao<DialogOccupant, Long> dialogOccupantDao;

    public UserDataManager(Dao<QMUser, Long> userDao, Dao<DialogOccupant, Long> dialogOccupantDao) {
        super(userDao, UserDataManager.class.getSimpleName());
        this.dialogOccupantDao = dialogOccupantDao;
    }

    public boolean isUserOwner(String email) {
        QMUser user = null;

        try {
            QueryBuilder<QMUser, Long> queryBuilder = dao.queryBuilder();
            queryBuilder.where().eq(QMUserColumns.EMAIL, email);
            PreparedQuery<QMUser> preparedQuery = queryBuilder.prepare();
            user = dao.queryForFirst(preparedQuery);
        } catch (SQLException e) {
            ErrorUtils.logError(e);
        }

        return user != null;
    }

    public QMUser getOwner() {
        QMUser user = null;

        try {
            QueryBuilder<QMUser, Long> queryBuilder = dao.queryBuilder();
//            queryBuilder.where().eq(QMUserColumns.ROLE, QMUser.Role.OWNER);
            PreparedQuery<QMUser> preparedQuery = queryBuilder.prepare();
            user = dao.queryForFirst(preparedQuery);
        } catch (SQLException e) {
            ErrorUtils.logError(e);
        }

        return user;
    }

    public List<QMUser> getAllByIds(List<Integer> idsList) {
        List<QMUser> usersList  = Collections.emptyList();

        try {
            QueryBuilder<QMUser, Long> queryBuilder = dao.queryBuilder();
            queryBuilder.where().in(QMUserColumns.ID, idsList);
            PreparedQuery<QMUser> preparedQuery = queryBuilder.prepare();
            usersList = dao.query(preparedQuery);
        } catch (SQLException e) {
            ErrorUtils.logError(e);
        }

        return usersList;
    }

    public List<QMUser> getUsersForGroupChat(String dialogId, List<Integer> idsList) {
        List<QMUser> usersList  = Collections.emptyList();

        try {
            QueryBuilder<QMUser, Long> userQueryBuilder = dao.queryBuilder();
            userQueryBuilder.where().in(QMUserColumns.ID, idsList);

            QueryBuilder<DialogOccupant, Long> dialogOccupantQueryBuilder = dialogOccupantDao.queryBuilder();
            Where<DialogOccupant, Long> where = dialogOccupantQueryBuilder.where();
            where.and(
                where.eq(Dialog.Column.ID, dialogId),
                where.eq(DialogOccupant.Column.STATUS, DialogOccupant.Status.ACTUAL)
            );

            userQueryBuilder.join(dialogOccupantQueryBuilder);
            userQueryBuilder.distinct();

            userQueryBuilder.orderBy(QMUserColumns.FULL_NAME, true);

            PreparedQuery<QMUser> preparedQuery = userQueryBuilder.prepare();
            usersList = dao.query(preparedQuery);
        } catch (SQLException e) {
            ErrorUtils.logError(e);
        }

        return usersList;
    }

    @Override
    protected void addIdToNotification(Bundle bundle, Object id) {

    }
}





//import android.os.Bundle;
//import com.j256.ormlite.dao.Dao;
//import com.j256.ormlite.stmt.PreparedQuery;
//import com.j256.ormlite.stmt.QueryBuilder;
//import com.j256.ormlite.stmt.Where;
//import com.quickblox.users.model.QBUser;
//import com.tama.q_municate_db.managers.base.BaseManager;
//import com.tama.q_municate_db.models.Dialog;
//import com.tama.q_municate_db.models.DialogOccupant;
//import com.tama.q_municate_db.utils.ErrorUtils;
//import java.sql.SQLException;
//import java.util.Collections;
//import java.util.List;
//
////chka  //pp
//public class UserDataManager extends BaseManager<QBUser> {
//
//    private static final String TAG = UserDataManager.class.getSimpleName();
//
//    private Dao<DialogOccupant, Long> dialogOccupantDao;
//
//    public UserDataManager(Dao<QBUser, Long> userDao, Dao<DialogOccupant, Long> dialogOccupantDao) {
//        super(userDao, UserDataManager.class.getSimpleName());
//        this.dialogOccupantDao = dialogOccupantDao;
//    }
//
//    public boolean isQBUserOwner(String email) {
//        QBUser user = null;
//
//        try {
//            QueryBuilder<QBUser, Long> queryBuilder = dao.queryBuilder();
////            queryBuilder.where().eq(QBUser.Column.EMAIL, email);
//            PreparedQuery<QBUser> preparedQuery = queryBuilder.prepare();
//            user = dao.queryForFirst(preparedQuery);
//        } catch (SQLException e) {
//            ErrorUtils.logError(e);
//        }
//
////        return user != null && user.getRole() == QBUser.Role.OWNER;
//        return user != null ;
//    }
//
//    public QBUser getOwner() {
//        QBUser user = null;
//
//        try {
//            QueryBuilder<QBUser, Long> queryBuilder = dao.queryBuilder();
////            queryBuilder.where().eq(QBUser.Column.ROLE, QBUser.Role.OWNER);
//            PreparedQuery<QBUser> preparedQuery = queryBuilder.prepare();
//            user = dao.queryForFirst(preparedQuery);
//        } catch (SQLException e) {
//            ErrorUtils.logError(e);
//        }
//
//        return user;
//    }
//
//    public List<QBUser> getAllByIds(List<Integer> idsList) {
//        List<QBUser> usersList  = Collections.emptyList();
//
//        try {
//            QueryBuilder<QBUser, Long> queryBuilder = dao.queryBuilder();
////            queryBuilder.where().in(QBUser.Column.ID, idsList);
//            PreparedQuery<QBUser> preparedQuery = queryBuilder.prepare();
////            QBUsersList = dao.query(preparedQuery);
//        } catch (SQLException e) {
//            ErrorUtils.logError(e);
//        }
//
////        return QBUsersList;
//        return null;
//    }
//
//    public List<QBUser> getQBQBUsersForGroupChat(String dialogId, List<Integer> idsList) {
//        List<QBUser> usersList  = Collections.emptyList();
//
//        try {
//            QueryBuilder<QBUser, Long> userQueryBuilder = dao.queryBuilder();
////            userQueryBuilder.where().in(QBUser.Column.ID, idsList);
//
//            QueryBuilder<DialogOccupant, Long> dialogOccupantQueryBuilder = dialogOccupantDao.queryBuilder();
//            Where<DialogOccupant, Long> where = dialogOccupantQueryBuilder.where();
//            where.and(
//                    where.eq(Dialog.Column.ID, dialogId),
//                    where.eq(DialogOccupant.Column.STATUS, DialogOccupant.Status.ACTUAL)
//            );
//
//            userQueryBuilder.join(dialogOccupantQueryBuilder);
//            userQueryBuilder.distinct();
//
////            userQueryBuilder.orderBy(QBUser.Column.FULL_NAME, true);
//
//            PreparedQuery<QBUser> preparedQuery = userQueryBuilder.prepare();
//            usersList = dao.query(preparedQuery);
//        } catch (SQLException e) {
//            ErrorUtils.logError(e);
//        }
//
//        return usersList;
//    }
//
//    @Override
//    protected void addIdToNotification(Bundle bundle, Object id) {
//
//    }
//}