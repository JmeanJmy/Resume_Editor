package com.example.jiangmingyu.minilinkedin.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.jiangmingyu.minilinkedin.util.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Created by jiangmingyu on 2017/11/26.
 */

public class Project implements Parcelable{
    public String id;
    public String name;
    public Date startDate;
    public Date endDate;
    public List<String> details;


    public Project(){ id = UUID.randomUUID().toString();}


    protected Project(Parcel in) {
        id = in.readString();
        name = in.readString();
        startDate = DateUtils.stringToDate(in.readString());
        endDate = DateUtils.stringToDate(in.readString());
        details = new ArrayList<>();
        in.readStringList(details);
    }

    public static final Creator<Project> CREATOR = new Creator<Project>() {
        @Override
        public Project createFromParcel(Parcel in) {
            return new Project(in);
        }

        @Override
        public Project[] newArray(int size) {

            return new Project[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(DateUtils.dateToString(startDate));
        dest.writeString(DateUtils.dateToString(endDate));
        dest.writeStringList(details);
    }
}
