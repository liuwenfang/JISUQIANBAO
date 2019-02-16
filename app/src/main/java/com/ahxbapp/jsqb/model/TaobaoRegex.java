package com.ahxbapp.jsqb.model;

/**
 * Created by Jayzhang on 16/11/17.
 */
public class TaobaoRegex {

    //列表
    public final  static String list_item="<div class=\"index-mod__order-container___1ur4-\"(.*?)</table></div></div>";    //订单列表
    public final  static String list_itemcheck=".*查看物流.*";  //筛选订单
    public final  static String list_item_date="<span class=\"bought-wrapper-mod__create-time___3gHYC\"(.*?)>(.*?)</span>";
    public final  static String list_item_todetail_url="<a href=\"//(((?!<a).)*)\" class(((?!<a).)*)订单详情</a>";  //详情url
    public  static final  int sprideDate=120;

    public final  static String Address_list_item ="<tr class=\"thead-tbl-address [\\s\\S]*?\">([\\s\\S]*?)</tr>";
    //public final  static String Address_list_data="<td>\\s*(.*?)\\s*</td>\\s*<td>\\s*(.*?)\\s*</td>\\s*<td>\\s*(.*?)\\s*<td>\\s*(.*?)\\s*</td>\\s*<td>\\s*(.*?)\\s*</td>\\s*<td>\\s*<a ";
    public final  static String Address_list_data="<td>\\s*(.*?)\\s*</td>\\s*<td>\\s*(.*?)\\s*</td>\\s*<td>\\s*(.*?)\\s*</td>\\s*<td>\\s*(.*?)\\s*</td>\\s*<td>\\s*(.*?)\\s*</td>\\s*<td>\\s*<a ";
    //详情
    public final  static String TaoBaoDetailDataRegex="var\\s*data\\s*=(.*?)\\s*</script>";
    public final  static String TmallDetailDataRegex="var\\s*detailData\\s*=(.*?)\\s*</script>";

    public final  static String TaoBaoAddressTotalRegex="收货地址\\s*：\\s*</dt>\\s*<dd>\\s*(.*?)\\s*</dd>";
    public final  static String TaoBaoaddressOrderTimeRegex="<span class=\"state\">拍下宝贝</span>\\s*<span class=\"datetime\">(.*?)</span>";
    public final  static String TaoBaoaddressOrderNumRegex="<dl>\\s*<dt>订单编号:</dt>\\s*<dd>\\s*(.*?)\\s*</dd>";
}
