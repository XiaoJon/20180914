package com.hjhq.teamface.basis.database;

import android.content.Context;
import android.util.Log;

import com.hjhq.teamface.basis.database.gen.DaoMaster;

import org.greenrobot.greendao.database.Database;

/**
 * Created by lx on 2017/7/12.
 */

public class DbHelper extends DaoMaster.OpenHelper {
    public DbHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {

        Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion);

        if (oldVersion >= newVersion) {
            return;
        }
        switch (oldVersion) {
            case 31:
                //PushMessage增加im_apr_id字段
                db.execSQL("ALTER TABLE 'PUSH_MESSAGE' ADD  'IM_APR_ID' String");
                db.execSQL("ALTER TABLE 'CONVERSATION' ADD  'ICON_COLOR' String");
                db.execSQL("ALTER TABLE 'CONVERSATION' ADD  'ICON_TYPE' String");
                db.execSQL("ALTER TABLE 'CONVERSATION' ADD  'ICON_URL' String");
                db.execSQL("ALTER TABLE 'MEMBER' ADD  'MOBILE_PHONE' String");
                break;
            case 32:
                db.execSQL("ALTER TABLE 'CONVERSATION' ADD  'ICON_COLOR' String");
                db.execSQL("ALTER TABLE 'CONVERSATION' ADD  'ICON_TYPE' String");
                db.execSQL("ALTER TABLE 'CONVERSATION' ADD  'ICON_URL' String");
                db.execSQL("ALTER TABLE 'MEMBER' ADD  'MOBILE_PHONE' String");
                break;
            case 33:
                //添加mobile_phone字段
                db.execSQL("ALTER TABLE 'MEMBER' ADD  'MOBILE_PHONE' String");
                break;
            default:
                break;
        }

        /*//手动建表
        ConversationDao.createTable(db,true);*/
    }
}
