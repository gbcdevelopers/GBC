package gbc.sa.vansales.utils;
/**
 * Created by Rakshit on 14-Dec-16.
 */

import android.os.AsyncTask;
import java.util.ArrayList;

public class Chain {

    private ArrayList<Link> links = new ArrayList<>();

    private Link done;
    private Link fail;

    private boolean running;
    public Chain(Link done) {
        this.done = done;
    }

    public void add(Link link) {
        if (!running) {
            links.add(link);
        }
    }

    public void setFail(Link fail) {
        this.fail = fail;
    }

    public void start() {
        running = true;

        new BladeRunner().execute(links.toArray(new Link[links.size()]));
    }

    public static class Link {
        public void run() throws Exception {
        }
    }

    private class BladeRunner extends AsyncTask<Link, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Link... params) {
            for (Link link : params) {
                try {
                    link.run();
                } catch (Exception e) {
                    return false;
                }
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean finished) {
            links.clear();

            running = false;

            try {
                if (finished) {
                    done.run();
                } else {
                    fail.run();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
