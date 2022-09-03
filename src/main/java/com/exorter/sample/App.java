package com.exorter.sample;

import com.azure.core.http.HttpClient;
import com.azure.core.http.ProxyOptions;
import com.azure.core.http.netty.NettyAsyncHttpClientBuilder;
import com.azure.monitor.opentelemetry.exporter.AzureMonitorExporterBuilder;
import com.azure.monitor.opentelemetry.exporter.AzureMonitorTraceExporter;
import com.azure.monitor.opentelemetry.exporter.implementation.utils.Strings;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;

import java.net.InetSocketAddress;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Export Telemetry via Azure Monitor OpenTelemetry Exporter");

        ProxyOptions proxyOptions = new ProxyOptions(ProxyOptions.Type.HTTP, new InetSocketAddress("localhost", 8888));

        HttpClient nettyHttpClient = new NettyAsyncHttpClientBuilder()
                .proxy(proxyOptions)
                .build();

        String connectionString = Strings.trimAndEmptyToNull(System.getenv("CONNECTION_STRING"));
        if (connectionString == null) {
            System.out.println("CONNECTION_STRING env var is required and has not been set yet. Exiting.");
            return;
        }

        AzureMonitorTraceExporter exporter =
                new AzureMonitorExporterBuilder()
                        .connectionString(connectionString)
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
        Thread.sleep(10000);
    }
}