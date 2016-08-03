/*
 * Copyright (C) 2015 Drakeet <drakeet.me@gmail.com>
 *
 * This file is part of Meizhi
 *
 * Meizhi is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Meizhi is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Meizhi.  If not, see <http://www.gnu.org/licenses/>.
 */

package top.wefor.now.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import top.wefor.now.data.model.entity.Gank;

/**
 * Created by drakeet on 8/11/15.
 */
public class GankDailyResult extends BaseResult {

    @SerializedName("error")
    public boolean error;

    @SerializedName("results")
    public Result results;

    @SerializedName("category")
    public ArrayList<String> category;

    public class Result {
        @SerializedName("Android") public ArrayList<Gank> androidList;
        @SerializedName("iOS") public ArrayList<Gank> iOSList;
        @SerializedName("App") public ArrayList<Gank> appList;
        @SerializedName("瞎推荐") public ArrayList<Gank> 瞎推荐List;
        @SerializedName("休息视频") public ArrayList<Gank> 休息视频List;
        @SerializedName("拓展资源") public ArrayList<Gank> 拓展资源List;
        @SerializedName("福利") public ArrayList<Gank> 妹纸List;
    }
}
