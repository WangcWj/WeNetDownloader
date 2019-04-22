package demo.wang.cn.download.response;

import android.util.Log;

import java.io.IOException;

import demo.wang.cn.download.callback.ProgressListener;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created to :
 *
 * @author WANG
 * @date 2019/4/15
 */
public class DownloadProgressResponseBody extends ResponseBody {
    private ResponseBody responseBody;
    private ProgressListener progressListener;
    private BufferedSource bufferedSource;

    public DownloadProgressResponseBody(ResponseBody responseBody, ProgressListener progressListener) {
        this.responseBody = responseBody;
        this.progressListener = progressListener;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (null == bufferedSource) {
            bufferedSource = Okio.buffer(createSource(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source createSource(Source source) {
        return new ForwardingSource(source) {
            long currentRead;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long read = super.read(sink, byteCount);
                currentRead += read != -1 ? read : 0;
                if (null != progressListener) {
                    progressListener.progress(responseBody.contentLength(), currentRead, read == -1);
                }
                return read;
            }
        };
    }
}
