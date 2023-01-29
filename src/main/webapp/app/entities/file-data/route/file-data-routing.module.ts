import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FileDataComponent } from '../list/file-data.component';
import { FileDataDetailComponent } from '../detail/file-data-detail.component';
import { FileDataUpdateComponent } from '../update/file-data-update.component';
import { FileDataRoutingResolveService } from './file-data-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const fileDataRoute: Routes = [
  {
    path: '',
    component: FileDataComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FileDataDetailComponent,
    resolve: {
      fileData: FileDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FileDataUpdateComponent,
    resolve: {
      fileData: FileDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FileDataUpdateComponent,
    resolve: {
      fileData: FileDataRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(fileDataRoute)],
  exports: [RouterModule],
})
export class FileDataRoutingModule {}
