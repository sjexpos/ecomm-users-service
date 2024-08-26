package io.oigres.ecomm.service.users.api;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.util.UriBuilder;

import com.google.gson.Gson;

import io.oigres.ecomm.service.users.Constants;
import io.oigres.ecomm.service.users.api.model.JacksonPageImpl;
import io.oigres.ecomm.service.users.api.model.PageResponse;
import io.oigres.ecomm.service.users.api.model.exception.GenericExceptionTracker;
import reactor.core.publisher.Mono;

public class MiddlewareProxy implements InvocationHandler {
    private final Logger log = Logger.getLogger(getClass().getName());

    private final WebClient webClient;
    private final Gson gson = new Gson();
    
    public MiddlewareProxy(final WebClient webClient) {
        this.webClient = webClient;
    }

    public WebClient getWebClient() {
        return this.webClient;
    }

    protected ResponseSpec buildResponseSpecFromRequestHeadersSpec(RequestHeadersSpec<?> request) {
        return request
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, this::error4xxHandling)
                .onStatus(HttpStatusCode::is5xxServerError, this::error5xxHandling);
    }

    protected <T> Mono<T> makeGet(RequestHeadersSpec<?> request, Class<T> responseClass) {
        return buildResponseSpecFromRequestHeadersSpec(request).bodyToMono(responseClass).contextCapture();
    }

    protected <T> Mono<T> makeGet(RequestHeadersSpec<?> request, ParameterizedTypeReference<T> responseTypeRef) {
        return buildResponseSpecFromRequestHeadersSpec(request).bodyToMono(responseTypeRef).contextCapture();
    }

    protected <T> Mono<T> makeDelete(RequestHeadersSpec<?> request, Class<T> responseClass) {
        return buildResponseSpecFromRequestHeadersSpec(request).bodyToMono(responseClass).contextCapture();
    }

    protected <T> Mono<T> makeDelete(RequestHeadersSpec<?> request, ParameterizedTypeReference<T> responseTypeRef) {
        return buildResponseSpecFromRequestHeadersSpec(request).bodyToMono(responseTypeRef).contextCapture();
    }

    protected RequestBodySpec buildRequestBodySpec(RequestBodySpec request) {
        return request
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);
    }

    protected ResponseSpec buildResponseSpecFromRequestBodySpec(RequestBodySpec request, Object requestData) {
        RequestBodySpec postBody = buildRequestBodySpec(request);
        ResponseSpec responseSpec = null;
        if (requestData != null) {
            responseSpec = postBody.body(BodyInserters.fromValue(requestData)).retrieve();
        } else {
            responseSpec = postBody.retrieve();
        }
        return responseSpec
                .onStatus(HttpStatusCode::is4xxClientError, this::error4xxHandling)
                .onStatus(HttpStatusCode::is5xxServerError, this::error5xxHandling);
    }

    protected <T> Mono<T> makePost(RequestBodySpec request, Object requestData, Class<T> responseClass) {
        return buildResponseSpecFromRequestBodySpec(request, requestData).bodyToMono(responseClass).contextCapture();
    }

    protected <T> Mono<T> makePost(RequestBodySpec request, Object requestData, ParameterizedTypeReference<T> responseTypeRef) {
        return buildResponseSpecFromRequestBodySpec(request, requestData).bodyToMono(responseTypeRef).contextCapture();
    }

    protected <T> Mono<T> makePut(RequestBodySpec request, Object requestData, Class<T> responseClass) {
        return buildResponseSpecFromRequestBodySpec(request, requestData).bodyToMono(responseClass).contextCapture();
    }

    protected <T> Mono<T> makePut(RequestBodySpec request, Object requestData, ParameterizedTypeReference<T> responseTypeRef) {
        return buildResponseSpecFromRequestBodySpec(request, requestData).bodyToMono(responseTypeRef).contextCapture();
    }

    protected <T> Mono<T> makePatch(RequestBodySpec request, Object requestData, Class<T> responseClass) {
        return buildResponseSpecFromRequestBodySpec(request, requestData).bodyToMono(responseClass).contextCapture();
    }

    protected <T> Mono<T> makePatch(RequestBodySpec request, Object requestData, ParameterizedTypeReference<T> responseTypeRef) {
        return buildResponseSpecFromRequestBodySpec(request, requestData).bodyToMono(responseTypeRef).contextCapture();
    }



    public <T> T get(Function<UriBuilder, URI> uriFunction, Class<T> responseClass) {
        return (T)makeGet(getWebClient().get().uri(uriFunction), responseClass).block();
    }

    public <T> T get(Function<UriBuilder, URI> uriFunction, ParameterizedTypeReference<T> responseTypeRef) {
        return (T)makeGet(getWebClient().get().uri(uriFunction), responseTypeRef).block();
    }

    public <T> PageResponse<T> getPage(Function<UriBuilder, URI> uriFunction, final Class<T> responseClass) {
        ParameterizedTypeReference<JacksonPageImpl<T>> typeReference = new ParameterizedTypeReference<JacksonPageImpl<T>>() {
            @Override
            public Type getType() {
                Type type = new ParameterizedType() {

                    @Override
                    public Type[] getActualTypeArguments() {
                        Type[] types = new Type[1];
                        types[0] = responseClass;
                        return types;
                    }

                    @Override
                    public Type getRawType() {
                        return JacksonPageImpl.class;
                    }

                    @Override
                    public Type getOwnerType() {
                        return JacksonPageImpl.class;
                    }
                };
                return type;
            }
        };
        return get(uriFunction, typeReference);
    }

    public <T> Future<T> getAsync(Function<UriBuilder, URI> uriFunction, Class<T> responseClass) {
        return (Future<T>)makeGet(getWebClient().get().uri(uriFunction), responseClass).toFuture();
    }

    public <T> Future<T> getAsync(Function<UriBuilder, URI> uriFunction, ParameterizedTypeReference<T> responseTypeRef) {
        return (Future<T>)makeGet(getWebClient().get().uri(uriFunction), responseTypeRef).toFuture();
    }

    public <T> Future<? extends PageResponse<T>> getPageAsync(Function<UriBuilder, URI> uriFunction, Class<T> responseClass) {
        ParameterizedTypeReference<JacksonPageImpl<T>> typeReference = new ParameterizedTypeReference<JacksonPageImpl<T>>() {};
        return getAsync(uriFunction, typeReference);
    }

    public <T> T post(Function<UriBuilder, URI> uriFunction, Object requestData, Class<T> responseClass) {
        return (T)makePost(getWebClient().post().uri(uriFunction), requestData, responseClass).block();
    }

    public <T> T post(Function<UriBuilder, URI> uriFunction, Object requestData, ParameterizedTypeReference<T> responseTypeRef) {
        return (T)makePost(getWebClient().post().uri(uriFunction), requestData, responseTypeRef).block();
    }

    public <T> Future<T> postAsync(Function<UriBuilder, URI> uriFunction, Object requestData, Class<T> elementClass) {
        return (Future<T>)makePost(getWebClient().post().uri(uriFunction), requestData, elementClass).toFuture();
    }

    public <T> Future<T> postAsync(Function<UriBuilder, URI> uriFunction, Object requestData, ParameterizedTypeReference<T> responseTypeRef) {
        return (Future<T>)makePost(getWebClient().post().uri(uriFunction), requestData, responseTypeRef).toFuture();
    }




    public <T> T put(Function<UriBuilder, URI> uriFunction, Object requestData, Class<T> responseClass) {
        return (T)makePut(getWebClient().put().uri(uriFunction), requestData, responseClass).block();
    }

    public <T> T put(Function<UriBuilder, URI> uriFunction, Object requestData, ParameterizedTypeReference<T> responseTypeRef) {
        return (T)makePut(getWebClient().put().uri(uriFunction), requestData, responseTypeRef).block();
    }

    public <T> Future<T> putAsync(Function<UriBuilder, URI> uriFunction, Object requestData, Class<T> elementClass) {
        return (Future<T>)makePut(getWebClient().put().uri(uriFunction), requestData, elementClass).toFuture();
    }

    public <T> Future<T> putAsync(Function<UriBuilder, URI> uriFunction, Object requestData, ParameterizedTypeReference<T> responseTypeRef) {
        return (Future<T>)makePut(getWebClient().put().uri(uriFunction), requestData, responseTypeRef).toFuture();
    }



    public <T> T delete(Function<UriBuilder, URI> uriFunction, Class<T> responseClass) {
        return (T)makeGet(getWebClient().delete().uri(uriFunction), responseClass).block();
    }

    public <T> T delete(Function<UriBuilder, URI> uriFunction, ParameterizedTypeReference<T> responseTypeRef) {
        return (T)makeGet(getWebClient().delete().uri(uriFunction), responseTypeRef).block();
    }

    public <T> Future<T> deleteAsync(Function<UriBuilder, URI> uriFunction, Class<T> responseClass) {
        return (Future<T>)makeDelete(getWebClient().delete().uri(uriFunction), responseClass).toFuture();
    }

    public <T> Future<T> deleteAsync(Function<UriBuilder, URI> uriFunction, ParameterizedTypeReference<T> responseTypeRef) {
        return (Future<T>)makeDelete(getWebClient().delete().uri(uriFunction), responseTypeRef).toFuture();
    }



    public <T> T patch(Function<UriBuilder, URI> uriFunction, Object requestData, Class<T> responseClass) {
        return (T)makePatch(getWebClient().patch().uri(uriFunction), requestData, responseClass).block();
    }

    public <T> T patch(Function<UriBuilder, URI> uriFunction, Class<T> responseClass) {
        return (T)makePatch(getWebClient().patch().uri(uriFunction), null, responseClass).block();
    }

    public <T> T patch(Function<UriBuilder, URI> uriFunction, Object requestData, ParameterizedTypeReference<T> responseTypeRef) {
        return (T)makePatch(getWebClient().patch().uri(uriFunction), requestData, responseTypeRef).block();
    }

    public <T> Future<T> patchAsync(Function<UriBuilder, URI> uriFunction, Object requestData, Class<T> elementClass) {
        return (Future<T>)makePatch(getWebClient().patch().uri(uriFunction), requestData, elementClass).toFuture();
    }

    public <T> Future<T> patchAsync(Function<UriBuilder, URI> uriFunction, Object requestData, ParameterizedTypeReference<T> responseTypeRef) {
        return (Future<T>)makePatch(getWebClient().patch().uri(uriFunction), requestData, responseTypeRef).toFuture();
    }




    public Mono<Exception> error4xxHandling(ClientResponse clientResponse) {
        return clientResponse.bodyToMono(String.class).contextCapture().flatMap(this::error4xxHandling);
    }

    private Mono<Exception> error4xxHandling(String errorBody) {
        GenericExceptionTracker tracker = gson.fromJson(errorBody, GenericExceptionTracker.class);
        if (MethodArgumentNotValidException.class.getName().equals(tracker.getException())) {
            RemoteMethodArgumentNotValidException exception = new RemoteMethodArgumentNotValidException();
            Map<?,?> map = gson.fromJson(errorBody, Map.class);
            if (map.containsKey("errors")) {
                List<?> errors = (List<?>)map.get("errors");
                if (!errors.isEmpty()) {
                    for (Object error : errors) {
                        String errorStr = gson.toJson(error);
                        RemoteMethodArgumentNotValidException.Error e = gson.fromJson(errorStr, RemoteMethodArgumentNotValidException.Error.class);
                        exception.addError(e);
                    }
                }
            }
            return Mono.error(exception);
        }
        try {
            Class<?> clazz = Class.forName(tracker.getException());
            Throwable throwable = (Throwable) clazz.getConstructor(String.class).newInstance(tracker.getMessage());
            return Mono.error(throwable);
        } catch (ClassNotFoundException e) {
            return Mono.error(new RuntimeException(tracker.getMessage()));
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            return Mono.error(e);
        }
    }

    public Mono<Exception> error5xxHandling(ClientResponse clientResponse) {
        return clientResponse.bodyToMono(String.class)
                            .flatMap(this::error5xxHandling);
    }

    private Mono<Exception> error5xxHandling(String errorBody) {
        return  Mono.error(new RuntimeException(Constants.ERROR_500_USER_MESSAGE));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            return method.invoke(this, args);
        } catch (InvocationTargetException e) {
            Throwable targetException = e.getTargetException();
            if (WebClientRequestException.class.isAssignableFrom(targetException.getClass())) {
                throw new RuntimeException(Constants.ERROR_500_USER_MESSAGE);
            }
            if (RemoteMethodArgumentNotValidException.class.isAssignableFrom( targetException.getClass())) {
                throw targetException;
            }
            if (RuntimeException.class.isAssignableFrom( targetException.getClass())) {
                Throwable originalException = targetException.getCause();
                Class<?>[] thrownExceptions = method.getExceptionTypes();
                for (int i = 0; i < thrownExceptions.length; i++) {
                    if (thrownExceptions[i].isAssignableFrom(originalException.getClass())) {
                        throw originalException;
                    }
                }
                throw targetException;
            }
            throw e;
        } catch (Throwable t) {
            log.log(Level.SEVERE, "", t);
            return null;
        }
    }

    public <T> T createExceptionProxy(Class<T> clazz) {
        return (T)Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class<?>[] { clazz }, this);
    }

}
