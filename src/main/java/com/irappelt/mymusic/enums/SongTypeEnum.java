package com.irappelt.mymusic.enums;

/**
 * Created with IntelliJ IDEA.
 *
 * @author iRappelt
 * @project: mymusic
 * @description:
 * @date: 2021/03/25 22:48
 * @version: v1.0
 */
public enum SongTypeEnum {

    /**
     * 歌曲类型
     */
    CLASSIC("经典", 1),
    POPULAR("流行", 2),
    ANCIENT("古风", 3),
    RAP("说唱", 4),
    CHINESE("华语", 5),
    EA("欧美",6),
    JK("日韩",7),
    DJ("DJ",8);


    private String songType;

    private Integer typeId;

    SongTypeEnum(String songType, Integer typeId){
        this.songType = songType;
        this.typeId = typeId;
    }

    public static Integer getTypeId (String songType) {
        for (SongTypeEnum songTypeEnum: SongTypeEnum.values()) {
            if (songTypeEnum.getSongType().equals(songType)) {
                return songTypeEnum.getTypeId();
            }
        }
        return null;
    }

    public static String getTypeName (Integer typeId) {
        for (SongTypeEnum songTypeEnum: SongTypeEnum.values()) {
            if (songTypeEnum.getTypeId().equals(typeId)) {
                return songTypeEnum.getSongType();
            }
        }
        return null;
    }

    public String getSongType() {
        return songType;
    }

    public void setSongType(String songType) {
        this.songType = songType;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }
}
