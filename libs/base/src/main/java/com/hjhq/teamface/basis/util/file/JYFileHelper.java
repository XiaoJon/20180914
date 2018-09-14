package com.hjhq.teamface.basis.util.file;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 手机或SD卡文件操作
 *
 * @author Kender
 * @email: panlk@jy35.cn
 */
public class JYFileHelper {

    private JYFileHelper() {
    }

    private static final String SDPATH = Environment.getExternalStorageDirectory().getPath() + "//";
    private static String filesPath;

    public static String getFilespath(Context context) {
        if (filesPath == null) {
            filesPath = context.getFilesDir().getPath() + "//";
        }
        return filesPath;
    }

    /**
     * 判断SD卡是否存在
     *
     * @return
     */
    public static boolean existSDCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * SD卡总容量
     *
     * @return
     */
    public static long getSDAllSize() {
        // 取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        // 获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        // 获取所有数据块数
        long allBlocks = sf.getBlockCount();
        // 返回SD卡大小
        return (allBlocks * blockSize) / 1024 / 1024; // 单位MB
    }

    /**
     * SD卡剩余空间
     *
     * @return
     */
    public static long getSDFreeSize() {
        // 取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        // 获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        // 空闲的数据块的数量
        long freeBlocks = sf.getAvailableBlocks();
        // 返回SD卡空闲大小
        return (freeBlocks * blockSize) / 1024 / 1024; // 单位MB
    }

    /**
     * 在SD卡上创建文件
     *
     * @throws IOException
     */
    public static File creatSDFile(String fileName) throws IOException {
        File file = new File(SDPATH + fileName);
        file.createNewFile();
        return file;
    }

    /**
     * 删除SD卡上的文件
     *
     * @param fileName
     */
    public static boolean delSDFile(String fileName) {
        File file = new File(SDPATH + fileName);
        if (!file.exists() || file.isDirectory())
            return false;
        file.delete();
        return true;
    }

    /**
     * 在SD卡上创建目录
     *
     * @param dirName
     */
    public static File creatSDDir(String dirName) {
        File dir = new File(SDPATH + dirName);
        boolean mkdir = true;
        if (!dir.exists())
            mkdir = dir.mkdirs();
        return dir;
    }

    /**
     * 在SD卡上创建目录
     *
     * @param dirName
     */
    public static File creatDir(Context context, String dirName) {
        File dir;
        if (existSDCard()) {
            dir = new File(SDPATH + dirName);
        } else {
            dir = new File(getFilespath(context) + dirName);
        }
        if (!dir.exists())
            dir.mkdirs();
        return dir;
    }


    /**
     * 删除SD卡上的目录
     *
     * @param dirName
     */
    public static boolean delSDDir(String dirName) {
        File dir = new File(SDPATH + dirName);
        return delDir(dir);
    }

    /**
     * 修改SD卡上的文件或目录名
     *
     * @param oldfileName
     * @param newFileName
     */
    public static boolean renameSDFile(String oldfileName, String newFileName) {
        File oleFile = new File(SDPATH + oldfileName);
        File newFile = new File(SDPATH + newFileName);
        return oleFile.renameTo(newFile);
    }

    /**
     * 拷贝SD卡上的单个文件
     *
     * @param srcFileName
     * @param destFileName
     * @throws IOException
     */
    public static boolean copySDFileTo(String srcFileName, String destFileName) throws IOException {
        File srcFile = new File(SDPATH + srcFileName);
        File destFile = new File(SDPATH + destFileName);
        return copyFileTo(srcFile, destFile);
    }

    /**
     * 拷贝SD卡上指定目录的所有文件
     *
     * @param srcDirName
     * @param destDirName
     * @return
     * @throws IOException
     */
    public static boolean copySDFilesTo(String srcDirName, String destDirName) throws IOException {
        File srcDir = new File(SDPATH + srcDirName);
        File destDir = new File(SDPATH + destDirName);
        return copyFilesTo(srcDir, destDir);
    }

    /**
     * 移动SD卡上的单个文件
     *
     * @param srcFileName
     * @param destFileName
     * @return
     * @throws IOException
     */
    public static boolean moveSDFileTo(String srcFileName, String destFileName) throws IOException {
        File srcFile = new File(SDPATH + srcFileName);
        File destFile = new File(SDPATH + destFileName);
        return moveFileTo(srcFile, destFile);
    }

    /**
     * 移动SD卡上的指定目录的所有文件
     *
     * @param srcDirName
     * @param destDirName
     * @return
     * @throws IOException
     */
    public static boolean moveSDFilesTo(String srcDirName, String destDirName) throws IOException {
        File srcDir = new File(SDPATH + srcDirName);
        File destDir = new File(SDPATH + destDirName);
        return moveFilesTo(srcDir, destDir);
    }

    /*
     * 判断文件是否有sd卡没有写入内存，有写入sd卡中("test.txt");
     */
    public static OutputStream writeFile(Context context, String fileName) throws IOException {
        File file = null;
        if (existSDCard()) {
            file = new File(SDPATH + fileName);
        } else {
            file = new File(getFilespath(context) + fileName);
        }

        FileOutputStream fos = new FileOutputStream(file);
        return fos;
    }

    /*
     * 在原有文件上继续写文件。如:appendSDFile("test.txt");
     */
    public static OutputStream appendSDFile(String fileName) throws IOException {
        File file = new File(SDPATH + fileName);
        FileOutputStream fos = new FileOutputStream(file, true);
        return fos;
    }

    /*
     * 从SD卡读取文件。如:readSDFile("test.txt");
     */
    public static InputStream readSDFile(String fileName) throws IOException {
        FileInputStream fis = new FileInputStream(SDPATH + fileName);
        return fis;
    }

    /**
     * 建立私有文件
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public static File creatDataFile(Context context, String fileName) throws IOException {
        File file = new File(getFilespath(context) + fileName);
        file.createNewFile();
        return file;
    }

    /**
     * 建立私有目录
     *
     * @param dirName
     * @return
     */
    public static File creatDataDir(Context context, String dirName) {
        File dir = new File(getFilespath(context) + dirName);
        dir.mkdir();
        return dir;
    }

    /**
     * 删除私有文件
     *
     * @param fileName
     * @return
     */
    public static boolean delDataFile(Context context, String fileName) {
        File file = new File(getFilespath(context) + fileName);
        return delFile(file);
    }

    /**
     * 删除私有目录
     *
     * @param dirName
     * @return
     */
    public static boolean delDataDir(Context context, String dirName) {
        File file = new File(getFilespath(context) + dirName);
        return delDir(file);
    }

    /**
     * 更改私有文件名
     *
     * @param oldName
     * @param newName
     * @return
     */
    public static boolean renameDataFile(Context context, String oldName, String newName) {
        File oldFile = new File(getFilespath(context) + oldName);
        File newFile = new File(getFilespath(context) + newName);
        return oldFile.renameTo(newFile);
    }

    /**
     * 在私有目录下进行文件复制
     *
     * @param srcFileName  ： 包含路径及文件名
     * @param destFileName
     * @return
     * @throws IOException
     */
    public static boolean copyDataFileTo(Context context, String srcFileName, String destFileName) throws IOException {
        File srcFile = new File(getFilespath(context) + srcFileName);
        File destFile = new File(getFilespath(context) + destFileName);
        return copyFileTo(srcFile, destFile);
    }

    /**
     * 复制私有目录里指定目录的所有文件
     *
     * @param srcDirName
     * @param destDirName
     * @return
     * @throws IOException
     */
    public static boolean copyDataFilesTo(Context context, String srcDirName, String destDirName) throws IOException {
        File srcDir = new File(getFilespath(context) + srcDirName);
        File destDir = new File(getFilespath(context) + destDirName);
        return copyFilesTo(srcDir, destDir);
    }

    /**
     * 移动私有目录下的单个文件
     *
     * @param srcFileName
     * @param destFileName
     * @return
     * @throws IOException
     */
    public static boolean moveDataFileTo(Context context, String srcFileName, String destFileName) throws IOException {
        File srcFile = new File(getFilespath(context) + srcFileName);
        File destFile = new File(getFilespath(context) + destFileName);
        return moveFileTo(srcFile, destFile);
    }

    /**
     * 移动私有目录下的指定目录下的所有文件
     *
     * @param srcDirName
     * @param destDirName
     * @return
     * @throws IOException
     */
    public static boolean moveDataFilesTo(Context context, String srcDirName, String destDirName) throws IOException {
        File srcDir = new File(getFilespath(context) + srcDirName);
        File destDir = new File(getFilespath(context) + destDirName);
        return moveFilesTo(srcDir, destDir);
    }

    /*
     * 将文件写入应用私有的files目录。如:writeFile("test.txt");
     */
    public static OutputStream wirteFile(Context context, String fileName) throws IOException {
        OutputStream os = context.openFileOutput(fileName, Context.MODE_PRIVATE);
        return os;
    }

    /*
     * 在原有文件上继续写文件。如:appendFile("test.txt");
     */
    public OutputStream appendFile(Context context, String fileName) throws IOException {
        OutputStream os = context.openFileOutput(fileName, Context.MODE_APPEND);
        return os;
    }

    /*
     * 从应用的私有目录files读取文件。如:readFile("test.txt");
     */
    public InputStream readFile(Context context, String fileName) throws IOException {
        InputStream is = context.openFileInput(fileName);
        return is;
    }

    /**********************************************************************************************************/
    /*********************************************************************************************************/

    /**
     * 删除一个文件
     *
     * @param file
     * @return
     */
    public static boolean delFile(File file) {
        if (file.isDirectory())
            return false;
        return file.delete();
    }

    /**
     * 删除一个目录（可以是非空目录）
     *
     * @param dir
     */
    public static boolean delDir(File dir) {
        return delDir(dir, true);
    }

    /**
     * 删除一个目录（可以是非空目录）
     *
     * @param dir
     * @param bl  是否删除文件夹
     */
    public static boolean delDir(File dir, boolean bl) {
        if (dir == null || !dir.exists() || dir.isFile()) {
            return false;
        }
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                delDir(file, bl);// 递归
            }
        }
        if (bl) {
            dir.delete();
        }
        return true;
    }

    /**
     * 拷贝一个文件,srcFile源文件，destFile目标文件
     *
     * @param srcFile
     * @param destFile
     * @throws IOException
     */
    public static boolean copyFileTo(File srcFile, File destFile) throws IOException {
        if (srcFile.isDirectory() || destFile.isDirectory()) {
            return false;// 判断是否是文件
        }
        FileInputStream fis = new FileInputStream(srcFile);
        FileOutputStream fos = new FileOutputStream(destFile);
        int readLen = 0;
        byte[] buf = new byte[1024];
        while ((readLen = fis.read(buf)) != -1) {
            fos.write(buf, 0, readLen);
        }
        fos.flush();
        fos.close();
        fis.close();
        return true;
    }

    /**
     * 拷贝目录下的所有文件到指定目录
     *
     * @param srcDir
     * @param destDir
     * @return
     * @throws IOException
     */
    public static boolean copyFilesTo(File srcDir, File destDir) throws IOException {
        if (!srcDir.isDirectory() || !destDir.isDirectory()) {
            return false;// 判断是否是目录
        }
        if (!destDir.exists()) {
            return false;// 判断目标目录是否存在
        }
        File[] srcFiles = srcDir.listFiles();
        for (int i = 0; i < srcFiles.length; i++) {
            if (srcFiles[i].isFile()) {
                // 获得目标文件
                File destFile = new File(destDir.getPath() + "//" + srcFiles[i].getName());
                copyFileTo(srcFiles[i], destFile);
            } else if (srcFiles[i].isDirectory()) {
                File theDestDir = new File(destDir.getPath() + "//" + srcFiles[i].getName());
                copyFilesTo(srcFiles[i], theDestDir);
            }
        }
        return true;
    }

    /**
     * 移动一个文件
     *
     * @param srcFile
     * @param destFile
     * @return
     * @throws IOException
     */
    public static boolean moveFileTo(File srcFile, File destFile) throws IOException {
        boolean iscopy = copyFileTo(srcFile, destFile);
        if (!iscopy)
            return false;
        delFile(srcFile);
        return true;
    }

    /**
     * 移动目录下的所有文件到指定目录
     *
     * @param srcDir
     * @param destDir
     * @return
     * @throws IOException
     */
    public static boolean moveFilesTo(File srcDir, File destDir) throws IOException {
        if (!srcDir.isDirectory() || !destDir.isDirectory()) {
            return false;
        }
        File[] srcDirFiles = srcDir.listFiles();
        for (int i = 0; i < srcDirFiles.length; i++) {
            if (srcDirFiles[i].isFile()) {
                File oneDestFile = new File(destDir.getPath() + "//" + srcDirFiles[i].getName());
                moveFileTo(srcDirFiles[i], oneDestFile);
                delFile(srcDirFiles[i]);
            } else if (srcDirFiles[i].isDirectory()) {
                File oneDestFile = new File(destDir.getPath() + "//" + srcDirFiles[i].getName());
                moveFilesTo(srcDirFiles[i], oneDestFile);
                delDir(srcDirFiles[i]);
            }

        }
        return true;
    }


    /**
     * 获得路径的文件夹
     */
    public static File getFileDir(Context context, String path) {
        File dir;
        if (existSDCard()) {
            dir = new File(SDPATH + path);
        } else {
            dir = new File(getFilespath(context) + path);
        }
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    /**
     * 该路径或者文件是否存在
     *
     * @param path
     * @return
     */
    public static boolean isExists(Context context, String path) {
        File dir;
        if (existSDCard()) {
            dir = new File(SDPATH + path);
        } else {
            dir = new File(getFilespath(context) + path);
        }
        return dir.exists();
    }

    /**
     * 拼接路径
     *
     * @param path
     * @return
     */
    public static String splicPath(Context context, String path) {
        if (existSDCard()) {
            return SDPATH + path;
        } else {
            return getFilespath(context) + path;
        }
    }

    // 递归方式 计算文件的大小
    public static long getTotalSizeOfFilesInDir(final File file) {
        if (file.isFile()) {
            return file.length();
        }
        final File[] children = file.listFiles();
        long total = 0;
        if (children != null) {
            for (final File child : children) {
                total += getTotalSizeOfFilesInDir(child);
            }
        }
        return total;
    }

    /**
     * 通过路径截取名字
     *
     * @param picturePath 路径
     * @return 文件名
     */
    public static String getPicNameFromPath(String picturePath) {
        String temp[] = picturePath.replaceAll("\\\\", "/").split("/");
        String fileName = "";
        if (temp.length > 1) {
            fileName = temp[temp.length - 1];
        }
        return fileName;
    }
}