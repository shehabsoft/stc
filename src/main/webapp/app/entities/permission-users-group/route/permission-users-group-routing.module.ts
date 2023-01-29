import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PermissionUsersGroupComponent } from '../list/permission-users-group.component';
import { PermissionUsersGroupDetailComponent } from '../detail/permission-users-group-detail.component';
import { PermissionUsersGroupUpdateComponent } from '../update/permission-users-group-update.component';
import { PermissionUsersGroupRoutingResolveService } from './permission-users-group-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const permissionUsersGroupRoute: Routes = [
  {
    path: '',
    component: PermissionUsersGroupComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PermissionUsersGroupDetailComponent,
    resolve: {
      permissionUsersGroup: PermissionUsersGroupRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PermissionUsersGroupUpdateComponent,
    resolve: {
      permissionUsersGroup: PermissionUsersGroupRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PermissionUsersGroupUpdateComponent,
    resolve: {
      permissionUsersGroup: PermissionUsersGroupRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(permissionUsersGroupRoute)],
  exports: [RouterModule],
})
export class PermissionUsersGroupRoutingModule {}
