package com.university;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

public class GetSubFolders {

    // com.google.api.services.drive.model.File
    public static final List<File> getGoogleSubFolders(String googleFolderIdParent) throws IOException {

        Drive driveService = GoogleDriveUtils.getDriveService();

        String pageToken = null;
        List<File> list = new ArrayList<File>();

        String query = null;
        if (googleFolderIdParent == null) {
            query = " mimeType = 'application/vnd.google-apps.folder' " //
                    + " and 'root' in parents";
        } else {
            query = " mimeType = 'application/vnd.google-apps.folder' " //
                    + " and '" + googleFolderIdParent + "' in parents";
        }

        do {
            FileList result = driveService.files().list().setQ(query).setSpaces("drive") //
                    // Fields will be assigned values: id, name, createdTime
                    .setFields("nextPageToken, files(id, name, createdTime)")//
                    .setPageToken(pageToken).execute();
            for (File file : result.getFiles()) {
                list.add(file);
            }
            pageToken = result.getNextPageToken();
        } while (pageToken != null);
        //
        return list;
    }

    // com.google.api.services.drive.model.File
    public static final List<File> getGoogleRootFolders() throws IOException {
        return getGoogleSubFolders(null);
    }

    public static void main(String[] args) throws IOException {

        System.out.println(new java.io.File(".").getAbsolutePath());
        List<File> googleRootFolders = getGoogleRootFolders();
        for (File folder : googleRootFolders) {

            System.out.println("Folder ID: " + folder.getId() + " --- Name: " + folder.getName());
            Drive client = GoogleDriveUtils.getDriveService();
            client.files().list().setQ("'" +folder.getId() + "' in parents").setSpaces("drive")
                    // Fields will be assigned values: id, name, createdTime
                    .setFields("nextPageToken, files(id, name, createdTime)")//
                    .setPageToken(null).execute().getFiles();
        }
    }

}
