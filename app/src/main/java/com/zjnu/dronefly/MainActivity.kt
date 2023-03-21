package com.zjnu.dronefly

import android.Manifest
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import dji.common.error.DJIError
import dji.common.error.DJISDKError
import dji.sdk.base.BaseComponent
import dji.sdk.base.BaseProduct
import dji.sdk.sdkmanager.DJISDKInitEvent
import dji.sdk.sdkmanager.DJISDKManager
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {
    // 需要申请的用户权限
    private val PERMISSION_LIST = arrayOf<String>(
        Manifest.permission.VIBRATE,  // 程序震动
        Manifest.permission.INTERNET,  // 访问互联网(可能产生网络流量)
        Manifest.permission.ACCESS_WIFI_STATE,  // 获取WiFi状态以及WiFi接入点信息
        Manifest.permission.WAKE_LOCK,  // 关闭屏幕时后台进程仍然执行
        Manifest.permission.ACCESS_COARSE_LOCATION,  // 获得模糊定位信息(通过基站或者WiFi信息)
        Manifest.permission.ACCESS_NETWORK_STATE,  // 获取网络状态信息
        Manifest.permission.ACCESS_FINE_LOCATION,  // 获得精准定位信息(通过定位卫星信号)
        Manifest.permission.CHANGE_WIFI_STATE,  // 改变WiFi连接状态
        Manifest.permission.WRITE_EXTERNAL_STORAGE,  // 写入外部存储
        Manifest.permission.BLUETOOTH,  // 配对蓝牙设备
        Manifest.permission.BLUETOOTH_ADMIN,  // 配对蓝牙设备(不通知用户)
        Manifest.permission.READ_EXTERNAL_STORAGE,  // 读取外部存储
        Manifest.permission.READ_PHONE_STATE
    )

    // 缺失的用户权限
    private val missingPermission = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (!checkPermissions()) {
            // 存在缺失权限，调用requestPermissions申请应用程序权限
            requestPermissions()
        }
        if (checkPermissions()) {
//            registerApplication()
        }

    }

    private fun registerApplication() {
        AsyncTask.execute {
            DJISDKManager.getInstance().registerApp(this.applicationContext,
                object : DJISDKManager.SDKManagerCallback {
                    override fun onRegister(djiError: DJIError) {

                        if (djiError == DJISDKError.REGISTRATION_SUCCESS) {
                            showToast("应用程序注册成功 ${djiError.description}")
                        } else {
                            showToast("应用程序注册失败 ${djiError.description}")
                        }
                    }

                    override fun onProductDisconnect() {
                        TODO("Not yet implemented")
                    }

                    override fun onProductConnect(p0: BaseProduct?) {
                        TODO("Not yet implemented")
                    }

                    override fun onProductChanged(p0: BaseProduct?) {
                        TODO("Not yet implemented")
                    }

                    override fun onComponentChange(
                        p0: BaseProduct.ComponentKey?,
                        p1: BaseComponent?,
                        p2: BaseComponent?
                    ) {
                        TODO("Not yet implemented")
                    }

                    override fun onInitProcess(djisdkInitEvent: DJISDKInitEvent, process: Int) {
                        showToast(djisdkInitEvent.initializationState.toString() + "进度${process}%")
                    }

                    override fun onDatabaseDownloadProgress(current: Long, total: Long) {
                        Log.v(
                            "fly database", "已下载${current}字节，总共：${total}字节" +
                                    ""
                        )
                    }

                })

        }
    }


    private fun checkPermissions(): Boolean {

        // 遍历所有Mobile SDK需要的权限
        for (permission in PERMISSION_LIST) {
            // 判断该权限是否已经被赋予
            if (ContextCompat.checkSelfPermission(this, permission) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                // 没有赋予的权限放入到missingPermission列表对象中
                this.missingPermission.add(permission)
            }
        }
        // 如果不存在缺失权限，则返回真；否则返回假
        return missingPermission.isEmpty()
    }

    private fun requestPermissions() {
        // 申请所有没有被赋予的权限
        ActivityCompat.requestPermissions(
            this,
            missingPermission.toTypedArray(),
            1009
        )
    }

    private fun showToast(text: String) {
        runOnUiThread {
            Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
        }

    }

}