import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PermissionUsersGroupComponent } from './list/permission-users-group.component';
import { PermissionUsersGroupDetailComponent } from './detail/permission-users-group-detail.component';
import { PermissionUsersGroupUpdateComponent } from './update/permission-users-group-update.component';
import { PermissionUsersGroupDeleteDialogComponent } from './delete/permission-users-group-delete-dialog.component';
import { PermissionUsersGroupRoutingModule } from './route/permission-users-group-routing.module';

@NgModule({
  imports: [SharedModule, PermissionUsersGroupRoutingModule],
  declarations: [
    PermissionUsersGroupComponent,
    PermissionUsersGroupDetailComponent,
    PermissionUsersGroupUpdateComponent,
    PermissionUsersGroupDeleteDialogComponent,
  ],
})
export class PermissionUsersGroupModule {}
