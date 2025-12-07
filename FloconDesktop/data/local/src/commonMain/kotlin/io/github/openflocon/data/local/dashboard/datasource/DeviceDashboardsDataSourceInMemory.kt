package io.github.openflocon.data.local.dashboard.datasource

import io.github.openflocon.data.core.dashboard.datasource.DeviceDashboardsDataSource
import io.github.openflocon.domain.dashboard.models.DashboardArrangementDomainModel
import io.github.openflocon.domain.dashboard.models.DashboardDomainModel
import io.github.openflocon.domain.dashboard.models.DashboardId
import io.github.openflocon.domain.device.models.DeviceIdAndPackageNameDomainModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class DeviceDashboardsDataSourceInMemory : DeviceDashboardsDataSource {
    private val selectedDeviceDashboards = MutableStateFlow<Map<DeviceIdAndPackageNameDomainModel, DashboardId?>>(emptyMap())
    private val selectedDashboardArrangements = MutableStateFlow<Map<DeviceIdAndPackageNameDomainModel, DashboardArrangementDomainModel>>(emptyMap())
    private val dashboards = MutableStateFlow<Map<Pair<DeviceIdAndPackageNameDomainModel, DashboardId>, DashboardDomainModel>>(emptyMap())

    override fun observeSelectedDeviceDashboard(deviceIdAndPackageName: DeviceIdAndPackageNameDomainModel): Flow<DashboardId?> = selectedDeviceDashboards
        .map { it[deviceIdAndPackageName] }
        .distinctUntilChanged()

    override fun selectDeviceDashboard(deviceIdAndPackageName: DeviceIdAndPackageNameDomainModel, dashboardId: DashboardId) {
        selectedDeviceDashboards.update {
            it + (deviceIdAndPackageName to dashboardId)
        }
    }

    override fun deleteDashboard(dashboardId: DashboardId, deviceIdAndPackageName: DeviceIdAndPackageNameDomainModel) {
        selectedDeviceDashboards.update {
            if (it[deviceIdAndPackageName] == dashboardId) {
                it - deviceIdAndPackageName
            } else it
        }
        dashboards.update {
            it - (deviceIdAndPackageName to dashboardId)
        }
    }

    override fun observeDashboardArrangement(deviceIdAndPackageName: DeviceIdAndPackageNameDomainModel): Flow<DashboardArrangementDomainModel> = selectedDashboardArrangements
        .map { it[deviceIdAndPackageName] ?: DashboardArrangementDomainModel.Adaptive }
        .distinctUntilChanged()

    override fun selectDashboardArrangement(
        deviceIdAndPackageName: DeviceIdAndPackageNameDomainModel,
        arrangement: DashboardArrangementDomainModel
    ) {
        selectedDashboardArrangements.update {
            it + (deviceIdAndPackageName to arrangement)
        }
    }

    override fun saveDashboard(
        deviceIdAndPackageName: DeviceIdAndPackageNameDomainModel,
        dashboard: DashboardDomainModel
    ) {
        dashboards.update {
            it + ((deviceIdAndPackageName to dashboard.dashboardId) to dashboard)
        }
    }

    override fun observeDashboard(
        deviceIdAndPackageName: DeviceIdAndPackageNameDomainModel,
        dashboardId: DashboardId
    ): Flow<DashboardDomainModel?> = dashboards
        .map { it[deviceIdAndPackageName to dashboardId] }
        .distinctUntilChanged()

    override fun observeDeviceDashboards(deviceIdAndPackageName: DeviceIdAndPackageNameDomainModel): Flow<List<DashboardId>> = dashboards
        .map { allDashboards ->
            allDashboards.keys
                .filter { it.first == deviceIdAndPackageName }
                .map { it.second }
        }
        .distinctUntilChanged()
}
