package com.anybeen.mark.imageeditor.mulitphoto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by maidou on 2016/4/21.
 */
public class ImagePropertyModel implements Parcelable{
    private String imagePath;
    private boolean isSelected;

    public ImagePropertyModel(String path, boolean isSelected) {
        this.imagePath = path;
        this.isSelected = isSelected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imagePath);
        dest.writeInt(isSelected ? 1 : 0);
    }

    public static final Creator<ImagePropertyModel> CREATOR = new Creator<ImagePropertyModel>() {
        @Override
        public ImagePropertyModel createFromParcel(Parcel source) {
            return new ImagePropertyModel(source.readString(), source.readInt() == 1 ? true : false);
        }

        @Override
        public ImagePropertyModel[] newArray(int size) {
            return new ImagePropertyModel[size];
        }
    };

    // split line


    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String path) {
        this.imagePath = path;
    }

    public boolean getSelecteState() {
        return isSelected;
    }

    public void setSelectedState(boolean selectedState) {
        this.isSelected = selectedState;
    }
}
