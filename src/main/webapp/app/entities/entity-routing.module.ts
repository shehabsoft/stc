import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'permission-groups',
        data: { pageTitle: 'filemanagerServiceApp.permissionGroups.home.title' },
        loadChildren: () => import('./permission-groups/permission-groups.module').then(m => m.PermissionGroupsModule),
      },
      {
        path: 'permission-users-group',
        data: { pageTitle: 'filemanagerServiceApp.permissionUsersGroup.home.title' },
        loadChildren: () => import('./permission-users-group/permission-users-group.module').then(m => m.PermissionUsersGroupModule),
      },
      {
        path: 'item',
        data: { pageTitle: 'filemanagerServiceApp.item.home.title' },
        loadChildren: () => import('./item/item.module').then(m => m.ItemModule),
      },
      {
        path: 'file-data',
        data: { pageTitle: 'filemanagerServiceApp.fileData.home.title' },
        loadChildren: () => import('./file-data/file-data.module').then(m => m.FileDataModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
