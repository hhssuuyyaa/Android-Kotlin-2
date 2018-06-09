package com.ayush171196.Startup



class PostInfo{
    var UserUID:String?=null
    var Text:String?=null
    var PostImage:String?=null
    constructor(UserUid:String,Text:String,PostImage:String)
    {
        this.UserUID=UserUid
        this.Text=Text
        this.PostImage=PostImage
    }
}