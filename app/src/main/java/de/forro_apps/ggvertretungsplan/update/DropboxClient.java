package de.forro_apps.ggvertretungsplan.update;

import android.content.Context;
import android.os.NetworkOnMainThreadException;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;

import java.io.IOException;

public class DropboxClient {

    /**
     * Token to access the Dropbox app
     */
    private final String ACCESS_TOKEN = ""; // Insert access token here


    private Context context;

    private DbxClientV2 client;

    public DropboxClient(Context context) throws DbxException, IOException {
        this.context = context;

        DbxRequestConfig config = new DbxRequestConfig("ggvertretung/dropbox");
        client = new DbxClientV2(config, ACCESS_TOKEN);
    }

    /**
     * @return {@code {@link DbxClientV2}}
     */
    private DbxClientV2 getClient() {
        return client;
    }

    /**
     * @return The newest available version
     * @throws IOException
     * @throws DbxException
     * @throws NetworkOnMainThreadException
     */
    public String getVersion() throws IOException, DbxException, NetworkOnMainThreadException {
        ListFolderResult result;
        result = getClient().files().listFolder("");
        for (Metadata data : result.getEntries()) {
            if (data.getName().startsWith("version ") && data.getName().endsWith(".txt")) {
                return data.getName().substring(8, data.getName().indexOf(".txt"));
            }
        }
        return "0";
    }
}
