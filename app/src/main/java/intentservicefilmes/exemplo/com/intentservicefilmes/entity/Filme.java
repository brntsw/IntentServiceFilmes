package intentservicefilmes.exemplo.com.intentservicefilmes.entity;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by BPardini on 22/12/2016.
 */

public class Filme implements Parcelable {

    private int id;
    private String originalTitle;
    private Bitmap poster;
    private String overview;
    private String releaseDate;

    public Filme(){}

    private Filme(Parcel in) {
        id = in.readInt();
        originalTitle = in.readString();
        poster = in.readParcelable(Bitmap.class.getClassLoader());
        overview = in.readString();
        releaseDate = in.readString();
    }

    public static final Creator<Filme> CREATOR = new Creator<Filme>() {
        @Override
        public Filme createFromParcel(Parcel in) {
            return new Filme(in);
        }

        @Override
        public Filme[] newArray(int size) {
            return new Filme[size];
        }
    };

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public Bitmap getPoster() {
        return poster;
    }

    public void setPoster(Bitmap poster) {
        this.poster = poster;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(originalTitle);
        parcel.writeParcelable(poster, i);
        parcel.writeString(overview);
        parcel.writeString(releaseDate);
    }
}
