package ekylibre.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

/**************************************
 * Created by pierre on 8/23/16.      *
 * ekylibre.zero.util for zero-android*
 *************************************/
public class ImageConverter
{
    private static byte[] getBytesFromBitmap(Bitmap bitmap)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return (stream.toByteArray());
    }

    public static String convertImageToBase64(Bitmap image)
    {
        String imgString = Base64.encodeToString(getBytesFromBitmap(image),
                Base64.NO_WRAP);
        return (imgString);
    }

    public static ArrayList<String> getImagesFromIssue(int issueID)
    {
        int count = 0;
        File picturesFile;
        ArrayList<String> imageBlock = new ArrayList<>();

        picturesFile = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString(), issueID + "_ISSUE_" + count++ + ".jpg");

        while (picturesFile.exists())
        {
            Bitmap image = BitmapFactory.decodeFile(picturesFile.getPath());
            imageBlock.add(convertImageToBase64(image));

            picturesFile = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES).toString(), issueID + "_ISSUE_" + count++ + ".jpg");
        }
        return (imageBlock);
    }

}
