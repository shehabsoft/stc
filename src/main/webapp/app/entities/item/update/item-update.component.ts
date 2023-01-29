import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ItemFormService, ItemFormGroup } from './item-form.service';
import { IItem } from '../item.model';
import { ItemService } from '../service/item.service';
import { IPermissionUsersGroup } from 'app/entities/permission-users-group/permission-users-group.model';
import { PermissionUsersGroupService } from 'app/entities/permission-users-group/service/permission-users-group.service';
import { ItemType } from 'app/entities/enumerations/item-type.model';

@Component({
  selector: 'jhi-item-update',
  templateUrl: './item-update.component.html',
})
export class ItemUpdateComponent implements OnInit {
  isSaving = false;
  item: IItem | null = null;
  itemTypeValues = Object.keys(ItemType);

  permissionUsersGroupsSharedCollection: IPermissionUsersGroup[] = [];

  editForm: ItemFormGroup = this.itemFormService.createItemFormGroup();

  constructor(
    protected itemService: ItemService,
    protected itemFormService: ItemFormService,
    protected permissionUsersGroupService: PermissionUsersGroupService,
    protected activatedRoute: ActivatedRoute
  ) {}

  comparePermissionUsersGroup = (o1: IPermissionUsersGroup | null, o2: IPermissionUsersGroup | null): boolean =>
    this.permissionUsersGroupService.comparePermissionUsersGroup(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ item }) => {
      this.item = item;
      if (item) {
        this.updateForm(item);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const item = this.itemFormService.getItem(this.editForm);
    if (item.id !== null) {
      this.subscribeToSaveResponse(this.itemService.update(item));
    } else {
      this.subscribeToSaveResponse(this.itemService.create(item));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IItem>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(item: IItem): void {
    this.item = item;
    this.itemFormService.resetForm(this.editForm, item);

    this.permissionUsersGroupsSharedCollection =
      this.permissionUsersGroupService.addPermissionUsersGroupToCollectionIfMissing<IPermissionUsersGroup>(
        this.permissionUsersGroupsSharedCollection,
        item.permissionUsersGroup
      );
  }

  protected loadRelationshipsOptions(): void {
    this.permissionUsersGroupService
      .query()
      .pipe(map((res: HttpResponse<IPermissionUsersGroup[]>) => res.body ?? []))
      .pipe(
        map((permissionUsersGroups: IPermissionUsersGroup[]) =>
          this.permissionUsersGroupService.addPermissionUsersGroupToCollectionIfMissing<IPermissionUsersGroup>(
            permissionUsersGroups,
            this.item?.permissionUsersGroup
          )
        )
      )
      .subscribe((permissionUsersGroups: IPermissionUsersGroup[]) => (this.permissionUsersGroupsSharedCollection = permissionUsersGroups));
  }
}
