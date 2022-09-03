package com.communication.quickstart;

import com.azure.core.http.HttpClient;
import com.azure.core.http.ProxyOptions;
import com.azure.core.http.netty.NettyAsyncHttpClientBuilder;
import com.azure.monitor.opentelemetry.exporter.AzureMonitorExporterBuilder;
import com.azure.monitor.opentelemetry.exporter.AzureMonitorTraceExporter;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;

import java.net.InetSocketAddress;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Azure Communication Services - Export Telemetry to Application Insights");

        ProxyOptions proxyOptions = new ProxyOptions(ProxyOptions.Type.HTTP, new InetSocketAddress("localhost", 8888));

        HttpClient nettyHttpClient = new NettyAsyncHttpClientBuilder()
                .proxy(proxyOptions)
                .build();

        AzureMonitorTraceExporter exporter =
                new AzureMonitorExporterBuilder()
                        .connectionString("{CONNECTION_STRING}")
                        .httpClient(nettyHttpClient)
                        .buildTraceExporter();

        SdkTracerProvider tracerProvider =
                SdkTracerProvider.builder().addSpanProcessor(SimpleSpanProcessor.create(exporter)).build();

        OpenTelemetrySdk openTelemetrySdk = OpenTelemetrySdk.builder()
                .setTracerProvider(tracerProvider)
                .build();

        Tracer tracer = openTelemetrySdk.getTracer("Sample");

        Span span = tracer.spanBuilder("sample-span").startSpan();
        final Scope scope = span.makeCurrent();
        span.end();
        scope.close();
        Thread.sleep(20000);
    }
}