import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IFileData, NewFileData } from '../file-data.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFileData for edit and NewFileDataFormGroupInput for create.
 */
type FileDataFormGroupInput = IFileData | PartialWithRequiredKeyOf<NewFileData>;

type FileDataFormDefaults = Pick<NewFileData, 'id'>;

type FileDataFormGroupContent = {
  id: FormControl<IFileData['id'] | NewFileData['id']>;
  binery: FormControl<IFileData['binery']>;
  bineryContentType: FormControl<IFileData['bineryContentType']>;
  item: FormControl<IFileData['item']>;
};

export type FileDataFormGroup = FormGroup<FileDataFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FileDataFormService {
  createFileDataFormGroup(fileData: FileDataFormGroupInput = { id: null }): FileDataFormGroup {
    const fileDataRawValue = {
      ...this.getFormDefaults(),
      ...fileData,
    };
    return new FormGroup<FileDataFormGroupContent>({
      id: new FormControl(
        { value: fileDataRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      binery: new FormControl(fileDataRawValue.binery),
      bineryContentType: new FormControl(fileDataRawValue.bineryContentType),
      item: new FormControl(fileDataRawValue.item),
    });
  }

  getFileData(form: FileDataFormGroup): IFileData | NewFileData {
    return form.getRawValue() as IFileData | NewFileData;
  }

  resetForm(form: FileDataFormGroup, fileData: FileDataFormGroupInput): void {
    const fileDataRawValue = { ...this.getFormDefaults(), ...fileData };
    form.reset(
      {
        ...fileDataRawValue,
        id: { value: fileDataRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): FileDataFormDefaults {
    return {
      id: null,
    };
  }
}
