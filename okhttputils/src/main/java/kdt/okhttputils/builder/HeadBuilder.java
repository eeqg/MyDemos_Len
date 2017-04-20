package kdt.okhttputils.builder;

import kdt.okhttputils.OkHttpUtils;
import kdt.okhttputils.request.OtherRequest;
import kdt.okhttputils.request.RequestCall;

/**
 * Created by zhy on 16/3/2.
 */
public class HeadBuilder extends GetBuilder
{
    @Override
    public RequestCall build()
    {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers,id).build();
    }
}
