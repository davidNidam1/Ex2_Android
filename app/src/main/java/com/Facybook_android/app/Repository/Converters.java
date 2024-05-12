    package com.Facybook_android.app.Repository;

    import android.content.res.Resources;
    import android.graphics.Bitmap;
    import android.graphics.BitmapFactory;
    import android.graphics.Canvas;
    import android.graphics.drawable.BitmapDrawable;
    import android.graphics.drawable.Drawable;
    import android.graphics.drawable.VectorDrawable;

    import androidx.room.TypeConverter;

    import com.Facybook_android.app.Model.entities.Comment;
    import com.Facybook_android.app.Model.entities.Token;
    import com.google.gson.Gson;
    import com.google.gson.reflect.TypeToken;

    import java.io.ByteArrayOutputStream;
    import java.lang.reflect.Type;
    import java.util.Date;
    import java.util.List;

    public class Converters {

        private static Gson gson = new Gson();

        @TypeConverter
        public static List<String> fromStringToStringList(String value) {
            Type listType = new TypeToken<List<String>>() {}.getType();
            return gson.fromJson(value, listType);
        }

        @TypeConverter
        public static String fromStringListToString(List<String> list) {
            return gson.toJson(list);
        }


        @TypeConverter
        public static List<Comment> fromString(String value) {
            return new Gson().fromJson(value, new TypeToken<List<Comment>>() {}.getType());
        }

        @TypeConverter
        public static String fromList(List<Comment> comments) {
            return new Gson().toJson(comments);
        }

        @TypeConverter
        public static byte[] drawableToByteArray(Drawable drawable) {
            if (drawable == null) {
                return null;
            }

            Bitmap bitmap;
            if (drawable instanceof BitmapDrawable) {
                bitmap = ((BitmapDrawable) drawable).getBitmap();
            } else if (drawable instanceof VectorDrawable) {
                bitmap = Bitmap.createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        Bitmap.Config.ARGB_8888
                );
                Canvas canvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                drawable.draw(canvas);
            } else {
                // Unsupported drawable type
                return null;
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            return outputStream.toByteArray();
        }

        @TypeConverter
        public static Drawable byteArrayToDrawable(byte[] byteArray) {
            if (byteArray == null) {
                return null;
            }

            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            return new BitmapDrawable(Resources.getSystem(), bitmap);
        }

        @TypeConverter
        public static Date fromTimestamp(Long value) {
            return value == null ? null : new Date(value);
        }

        @TypeConverter
        public static Long dateToTimestamp(Date date) {
            return date == null ? null : date.getTime();
        }
    }
