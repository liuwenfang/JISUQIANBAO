package com.ahxbapp.jsqb.model;

/**
 * Created by Jayzhang on 16/11/17.
 */
public class AlipayRegex {
    //详情
    public final  static String AlipayEmailRegex="<th>邮箱</th>\\s*<td>\\s*<span class=\"text-primary\">(.*?)</span>";
    public final  static String AlipayPhoneRegex="<th>手机</th>\\s*<td>\\s*<span class=\"text-muted\">(.*?)</span>";
    public final  static String AlipayRealNameRegex="<th>真实姓名</th>\\s*<td>\\s*<span\\s*id=\"username\">(.*?)</span>";
    public final  static String AlipayTaoBaoRegex="<th>淘宝会员名</th>\\s*<td>(.*?)</td>";

    public final static String AlipayHuabeiHuanRegex="<span class=\"tocal-amount\">(.*?)</span>";
    public final static String AlipayHuabeiYongRegex="<p>可用额度</p>\\s*<p><strong class=\"highlight\">(.*?)</strong>";
    public final static String AlipayHuabeiZongRegex="总额度:(.*?)元";
}
