package eu.tutorial.androidapplicationfilesystem.classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Playlist implements Parcelable{

    public static final Creator<Playlist> CREATOR = new Creator<Playlist>() {
        @Override
        public Playlist createFromParcel(Parcel in) {
            return new Playlist(in);
        }

        @Override
        public Playlist[] newArray(int size) {
            return new Playlist[size];
        }
    };

    String playlistName;
    ArrayList <MusicData> songs;
    String date;
    Bitmap bitmap;

    public Playlist(String playlistName, ArrayList<MusicData> songs, Context context) {
        songs = new ArrayList<>();
        this.playlistName = playlistName;
        this.songs = songs;
        this.date = setInitialDate();
        setPlaylistBitmap(context);
    }
    public Playlist (String playlistName) {
        songs = new ArrayList<>();
        this.playlistName = playlistName;
        this.date = setInitialDate();
    }

    public Playlist (String playlistName, String date) {
        songs = new ArrayList<>();
        this.playlistName = playlistName;
        this.date = date;
    }

    protected Playlist(Parcel in) {
        playlistName = in.readString();
        date = in.readString();
        bitmap = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public void setSong (int index){
        if(songs.get(index)==null){
            songs.add(index,null);
        }
    }

    public void resetPlaylistBitmap(Context context){
        bitmap = null;
        retrieveBitmap(context);
    }

    public void setPlaylistBitmap(Context context){
        if(bitmap == null) {
            retrieveBitmap(context);
        }
    }



    public Bitmap getPlaylistBitmap(Context context){
        setPlaylistBitmap(context);
        return bitmap;
    }

    public void retrieveBitmap(Context context){
        for (MusicData s : songs) {
            bitmap = s.loadBmap(context);
            if (bitmap != null) {
                break;
            }
        }
    }


    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public String getDate(){
        return date.toString();
    }

    public String getSongDate(String path){
        for(MusicData s: songs){
            if (path.equals(s.getPath())) {
                return s.getDate();
            }
        }
        return "";
    }

    public ArrayList<MusicData> getSongsArray() {
        return songs;
    }

    public void setSongsArray(ArrayList <MusicData> songs){
        this.songs = songs;
    }

    public void printSongsArray(){
        for(MusicData s: songs){
            System.out.println(s.getPath());
        }
    }

    public String getSongPath(int position){
        return songs.get(position).getPath();
    }

    public int getLength(){
        if(songs!=null){return songs.size();}
        else{return 0;}
    }

    public MusicData getSong(int index){
            return songs.get(index);
    }

    public MusicData getSong(String path){
        for(MusicData s: songs){
            //System.out.println(s.getPath()+" "+path);
            if (path.equals(s.getPath())) {
                return s;
            }
        }
        return null;
    }


    public boolean inPlaylist(String path) {
        boolean matches = false;
        for(MusicData s: songs){
            //System.out.println(s.getPath()+" "+path);
            if (path.equals(s.getPath())) {
                matches = true;
                break;
            }
        }
        return matches;
    }

    public boolean inPlaylist(File file) {
        String path = file.getAbsolutePath();
        boolean matches = false;
        for(MusicData s: songs){
            if (path.equals(s.getPath())) {
                matches = true;
                break;
            }
        }
        return matches;
    }

    public void addSong(File file, Context context){
        songs.add(new MusicData(file, context));
        setPlaylistBitmap(context);
    }

    public void addSong(File file, String date, Context context){
        songs.add(new MusicData(file, date));
        setPlaylistBitmap(context);
    }
    public void addSong(String path, String date, Context context){
        songs.add(new MusicData(path, date));
        setPlaylistBitmap(context);
    }
    public void addSong(String path, Context context){
        songs.add(new MusicData(path, context));
        setPlaylistBitmap(context);
    }

    public boolean removeSong(String path, Context context) {
        boolean deleted = false;
        for(MusicData s: songs){
            if (path.equals(s.getPath())) {
                deleted = true;
                songs.remove(s);
                break;
            }
        }
        resetPlaylistBitmap(context);
        return deleted;
    }

    public boolean removeSong(File file, Context context) {
        String path = file.getAbsolutePath();
        boolean deleted = false;
        for(MusicData s: songs){
            if (path.equals(s.getPath())) {
                songs.remove(s);
                deleted = true;
                for(int i=0; i<5; i++){System.out.println(deleted);}
                break;
            }
        }
        resetPlaylistBitmap(context);
        return deleted;
    }

    private String setInitialDate(){
        Date date = new Date();
        String dateFormat = "dd/MM/Y hh:mm:ss a";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        String formattedDate = sdf.format(date);
        System.out.println(setDateJavaUtil(formattedDate));
        return formattedDate;
    }

    private Date setDateJavaUtil(String dateString){

        String dateFormat = "dd/MM/Y hh:mm:ss a";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        Date date = null;
        try {
            date = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(playlistName);
        dest.writeString(date);
        dest.writeParcelable(bitmap, flags);
    }
}
