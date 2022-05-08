import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DetailOrderComponent } from './list/detail-order.component';
import { DetailOrderDetailComponent } from './detail/detail-order-detail.component';
import { DetailOrderUpdateComponent } from './update/detail-order-update.component';
import { DetailOrderDeleteDialogComponent } from './delete/detail-order-delete-dialog.component';
import { DetailOrderRoutingModule } from './route/detail-order-routing.module';

@NgModule({
  imports: [SharedModule, DetailOrderRoutingModule],
  declarations: [DetailOrderComponent, DetailOrderDetailComponent, DetailOrderUpdateComponent, DetailOrderDeleteDialogComponent],
  entryComponents: [DetailOrderDeleteDialogComponent],
})
export class DetailOrderModule {}
