package com.ruogu.codersanswerpro;

import android.app.Activity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by rogers on 3/5/14.
 */
public class StarredFileHandler {
    private Activity mActivity;
    private ArrayList<String> mStarredList;
    private static StarredFileHandler starredFileHandler;
    String mPath;

    private StarredFileHandler(Activity activity) {
        mActivity = activity;
        mStarredList = new ArrayList<String>();

        mPath = mActivity.getFilesDir() + "/starred.txt";
        try {
            BufferedReader inputStreamReader = new BufferedReader(new FileReader(mPath));
            StringBuilder stringBuilder = new StringBuilder();
            String content = "";
            while ((content = inputStreamReader.readLine()) != null) {
                stringBuilder.append(content);
            }

            for (String tmp : stringBuilder.toString().split(",")) {
                if (!tmp.equals("")) {
                    mStarredList.add(tmp);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            try {
                FileWriter fileWriter = new FileWriter(mPath);
                fileWriter.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public static StarredFileHandler getInstance(Activity activity) {
        if (starredFileHandler != null) {
            return starredFileHandler;
        } else {
            starredFileHandler = new StarredFileHandler(activity);
            return starredFileHandler;
        }
    }

    public boolean isStarred(String problem_name) {
        return mStarredList.contains(problem_name);
    }

    public void addToStarred(String problem_name) {
        if (isStarred(problem_name)) {
            return;
        } else {
            mStarredList.add(problem_name);
            push();
        }
    }

    public void deleteFromStarred(String problem_name) {
        mStarredList.remove(problem_name);
        push();
    }

    public ArrayList<String> getStarredList() {
        return mStarredList;
    }

    protected void push() {
        try {
            PrintWriter printWriter = new PrintWriter(new FileWriter(mPath));
            for (int i = 0; i < mStarredList.size() - 1; ++i) {
                printWriter.print(mStarredList.get(i));
                printWriter.print(",");
            }
            if (mStarredList.size() > 0) {
                printWriter.print(mStarredList.get(mStarredList.size() - 1));
            }
            printWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
