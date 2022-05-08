import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MeasureUnitComponent } from './list/measure-unit.component';
import { MeasureUnitDetailComponent } from './detail/measure-unit-detail.component';
import { MeasureUnitUpdateComponent } from './update/measure-unit-update.component';
import { MeasureUnitDeleteDialogComponent } from './delete/measure-unit-delete-dialog.component';
import { MeasureUnitRoutingModule } from './route/measure-unit-routing.module';

@NgModule({
  imports: [SharedModule, MeasureUnitRoutingModule],
  declarations: [MeasureUnitComponent, MeasureUnitDetailComponent, MeasureUnitUpdateComponent, MeasureUnitDeleteDialogComponent],
  entryComponents: [MeasureUnitDeleteDialogComponent],
})
export class MeasureUnitModule {}
