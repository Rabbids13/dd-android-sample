package com.example.datadogrumandroidsample

import android.app.Application
import android.util.Log
import com.datadog.android.Datadog
import com.datadog.android.DatadogSite
import com.datadog.android.core.configuration.BatchSize
import com.datadog.android.core.configuration.Configuration
import com.datadog.android.core.configuration.UploadFrequency
//import com.datadog.android.log.Logs
//import com.datadog.android.log
import com.datadog.android.privacy.TrackingConsent
import com.datadog.android.rum.GlobalRumMonitor
import com.datadog.android.rum.Rum
import com.datadog.android.rum.RumConfiguration
import com.datadog.android.rum.tracking.ActivityViewTrackingStrategy
import com.datadog.android.sessionreplay.SessionReplay
import com.datadog.android.sessionreplay.SessionReplayConfiguration
import com.datadog.android.sessionreplay.SessionReplayPrivacy
import com.datadog.android.trace.TraceConfiguration
import com.datadog.android.trace.Trace
import com.datadog.android.trace.AndroidTracer
import io.opentracing.util.GlobalTracer

class HyunjinApplication : Application() {
	override fun onCreate() {

		super.onCreate()
		Log.i("rum_test", "oncreate")

		initRum()

		Log.i("rum_test", "init Complete")
	}

	private fun initRum() {
        Datadog.setVerbosity(Log.DEBUG)

        val clientToken = "pubd6cd280cffccdbca312a9abb2b8400ae"
        val applicationId = "adf89367-0027-48d9-b103-29a9063ccce0"
        val environment = "stg"
        val appVariant = "DatadogApp"

        // 1. Initialize Datadog Core
        val config = Configuration.Builder(
            clientToken = clientToken,
            env = environment,
            variant = appVariant
        )
            .useSite(DatadogSite.US1)
            .setBatchSize(BatchSize.SMALL)
            .setUploadFrequency(UploadFrequency.FREQUENT)
            .build()

        Datadog.initialize(this, config, TrackingConsent.GRANTED)

        // 2. Enable RUM
        val rumConfig = RumConfiguration.Builder(applicationId)
            .useViewTrackingStrategy(ActivityViewTrackingStrategy(true))
            .trackUserInteractions()
            .setTelemetrySampleRate(100f)
            .build()

        Rum.enable(rumConfig)

        val sessionReplayConfig = SessionReplayConfiguration.Builder(100f)
            .build()
        SessionReplay.enable(sessionReplayConfig)

//        // 3. Enable Logs
//        Logs.enable(LogsConfiguration.Builder().build())

        // Optional debug
        GlobalRumMonitor.get().debug = true

        Log.i("rum_test", "Datadog init complete")

        val traceConfig = TraceConfiguration.Builder().build()
        Trace.enable(traceConfig)

        val tracer = AndroidTracer.Builder().setSampleRate(100.0).build()
        GlobalTracer.registerIfAbsent(tracer)

//		val clientToken = "pubd6cd280cffccdbca312a9abb2b8400ae"
//		val applicationId = "adf89367-0027-48d9-b103-29a9063ccce0"
//
//		val environmentName = "stg"
//		val appVariantName = "DatadogApp"
//
//		val configuration = Configuration.Builder(
//			logsEnabled = true,
//			tracesEnabled = true,
//			crashReportsEnabled = true,
//			rumEnabled = true
//		)
//			.trackInteractions()
//			.useSite(DatadogSite.US1)
//			.build()
//
//		val credentials = Credentials(clientToken, environmentName, appVariantName, applicationId)
//		Datadog.initialize(this, credentials, configuration, TrackingConsent.GRANTED)
//		// Session 샘플레이트
//		GlobalRum.registerIfAbsent(RumMonitor.Builder().sampleRumSessions(100.0f).build())
	}
}
