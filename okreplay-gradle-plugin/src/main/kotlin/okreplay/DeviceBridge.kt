package okreplay

import com.android.build.gradle.internal.LoggerWrapper
import com.android.builder.testing.ConnectedDeviceProvider
import com.android.builder.testing.api.DeviceException
import org.gradle.api.logging.Logger
import java.io.File

open class DeviceBridge(adbPath: File, adbTimeoutMs: Int, private val logger: Logger) {
  private val deviceProvider = ConnectedDeviceProvider(adbPath, adbTimeoutMs, LoggerWrapper(logger))

  internal fun useDevices(block: (Device) -> Unit) {
    try {
      deviceProvider.init()
      deviceProvider.devices
          .map { Device(DeviceInterface.newInstance(it), logger) }
          .forEach(block)
    } catch (e: DeviceException) {
      logger.warn(e.message)
    } finally {
      deviceProvider.terminate()
    }
  }
}

