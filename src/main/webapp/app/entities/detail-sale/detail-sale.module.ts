import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DetailSaleComponent } from './list/detail-sale.component';
import { DetailSaleDetailComponent } from './detail/detail-sale-detail.component';
import { DetailSaleUpdateComponent } from './update/detail-sale-update.component';
import { DetailSaleDeleteDialogComponent } from './delete/detail-sale-delete-dialog.component';
import { DetailSaleRoutingModule } from './route/detail-sale-routing.module';

@NgModule({
  imports: [SharedModule, DetailSaleRoutingModule],
  declarations: [DetailSaleComponent, DetailSaleDetailComponent, DetailSaleUpdateComponent, DetailSaleDeleteDialogComponent],
  entryComponents: [DetailSaleDeleteDialogComponent],
})
export class DetailSaleModule {}
