import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PermissionGroupsComponent } from './list/permission-groups.component';
import { PermissionGroupsDetailComponent } from './detail/permission-groups-detail.component';
import { PermissionGroupsUpdateComponent } from './update/permission-groups-update.component';
import { PermissionGroupsDeleteDialogComponent } from './delete/permission-groups-delete-dialog.component';
import { PermissionGroupsRoutingModule } from './route/permission-groups-routing.module';

@NgModule({
  imports: [SharedModule, PermissionGroupsRoutingModule],
  declarations: [
    PermissionGroupsComponent,
    PermissionGroupsDetailComponent,
    PermissionGroupsUpdateComponent,
    PermissionGroupsDeleteDialogComponent,
  ],
})
export class PermissionGroupsModule {}
