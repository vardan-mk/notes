package am.vardanmk.etl;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import org.slf4j.Logger;
import org.springframework.context.annotation.Profile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static org.slf4j.LoggerFactory.getLogger;


//@Profile("prod")
//public class LambdaHandler implements RequestStreamHandler {
//    private static SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;
//    static {
//        try {
//            handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(EtlApplication.class);
//        } catch (ContainerInitializationException e) {
//            // if we fail here. We re-throw the exception to force another cold start
//            e.printStackTrace();
//            throw new RuntimeException("Could not initialize Spring Boot application", e);
//        }
//    }
//
//    @Override
//    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context)
//            throws IOException {
//        handler.proxyStream(inputStream, outputStream, context);
//    }
//}

@Profile("prod")
public class LambdaHandler implements RequestStreamHandler {

    private static Logger logger = getLogger(LambdaHandler.class);

    public static SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;

    static {
        try {
            handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(EtlApplication.class);
            handler.activateSpringProfiles("prod");
        } catch (ContainerInitializationException e) {
            String errMsg = "Could not initialize Spring Boot application";
            logger.error(errMsg);
            throw new RuntimeException(errMsg, e);
        }
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        handler.proxyStream(inputStream, outputStream, context);
        outputStream.close();
    }
}