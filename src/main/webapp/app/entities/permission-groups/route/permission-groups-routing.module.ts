import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PermissionGroupsComponent } from '../list/permission-groups.component';
import { PermissionGroupsDetailComponent } from '../detail/permission-groups-detail.component';
import { PermissionGroupsUpdateComponent } from '../update/permission-groups-update.component';
import { PermissionGroupsRoutingResolveService } from './permission-groups-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const permissionGroupsRoute: Routes = [
  {
    path: '',
    component: PermissionGroupsComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PermissionGroupsDetailComponent,
    resolve: {
      permissionGroups: PermissionGroupsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PermissionGroupsUpdateComponent,
    resolve: {
      permissionGroups: PermissionGroupsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PermissionGroupsUpdateComponent,
    resolve: {
      permissionGroups: PermissionGroupsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(permissionGroupsRoute)],
  exports: [RouterModule],
})
export class PermissionGroupsRoutingModule {}
