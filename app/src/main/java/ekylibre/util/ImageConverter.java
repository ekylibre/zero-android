package ekylibre.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**************************************
 * Created by pierre on 8/23/16.      *
 * ekylibre.zero.util for zero-android*
 *************************************/
public class ImageConverter
{
    /*
    ** @return byteArray from bitmap
    */
    private static byte[] getBytesFromBitmap(Bitmap bitmap)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return (stream.toByteArray());
    }

    /*
    ** @return byteArray from base64 String
    */
    public static byte[] getByteArrayFromBase64(String image)
    {
        byte[] decodedString = Base64.decode(image, Base64.URL_SAFE);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return (getBytesFromBitmap(bitmap));
    }

    /*
    ** @return output stream from base64 String
    */
    public static ByteArrayOutputStream createStreamFromBase64(String image)
    {
        byte[] decodedString = Base64.decode(image, Base64.URL_SAFE);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return (stream);
    }

    /*
    ** @return base64 String form Bitmap image
    */
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
