import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'document-type',
        data: { pageTitle: 'panysoftApp.documentType.home.title' },
        loadChildren: () => import('./document-type/document-type.module').then(m => m.DocumentTypeModule),
      },
      {
        path: 'person',
        data: { pageTitle: 'panysoftApp.person.home.title' },
        loadChildren: () => import('./person/person.module').then(m => m.PersonModule),
      },
      {
        path: 'client',
        data: { pageTitle: 'panysoftApp.client.home.title' },
        loadChildren: () => import('./client/client.module').then(m => m.ClientModule),
      },
      {
        path: 'employee',
        data: { pageTitle: 'panysoftApp.employee.home.title' },
        loadChildren: () => import('./employee/employee.module').then(m => m.EmployeeModule),
      },
      {
        path: 'purchase-receipt',
        data: { pageTitle: 'panysoftApp.purchaseReceipt.home.title' },
        loadChildren: () => import('./purchase-receipt/purchase-receipt.module').then(m => m.PurchaseReceiptModule),
      },
      {
        path: 'detail-sale',
        data: { pageTitle: 'panysoftApp.detailSale.home.title' },
        loadChildren: () => import('./detail-sale/detail-sale.module').then(m => m.DetailSaleModule),
      },
      {
        path: 'product-elaborated',
        data: { pageTitle: 'panysoftApp.productElaborated.home.title' },
        loadChildren: () => import('./product-elaborated/product-elaborated.module').then(m => m.ProductElaboratedModule),
      },
      {
        path: 'category',
        data: { pageTitle: 'panysoftApp.category.home.title' },
        loadChildren: () => import('./category/category.module').then(m => m.CategoryModule),
      },
      {
        path: 'measure-unit',
        data: { pageTitle: 'panysoftApp.measureUnit.home.title' },
        loadChildren: () => import('./measure-unit/measure-unit.module').then(m => m.MeasureUnitModule),
      },
      {
        path: 'recip',
        data: { pageTitle: 'panysoftApp.recip.home.title' },
        loadChildren: () => import('./recip/recip.module').then(m => m.RecipModule),
      },
      {
        path: 'product',
        data: { pageTitle: 'panysoftApp.product.home.title' },
        loadChildren: () => import('./product/product.module').then(m => m.ProductModule),
      },
      {
        path: 'inventory',
        data: { pageTitle: 'panysoftApp.inventory.home.title' },
        loadChildren: () => import('./inventory/inventory.module').then(m => m.InventoryModule),
      },
      {
        path: 'presentation',
        data: { pageTitle: 'panysoftApp.presentation.home.title' },
        loadChildren: () => import('./presentation/presentation.module').then(m => m.PresentationModule),
      },
      {
        path: 'order-placed',
        data: { pageTitle: 'panysoftApp.orderPlaced.home.title' },
        loadChildren: () => import('./order-placed/order-placed.module').then(m => m.OrderPlacedModule),
      },
      {
        path: 'detail-order',
        data: { pageTitle: 'panysoftApp.detailOrder.home.title' },
        loadChildren: () => import('./detail-order/detail-order.module').then(m => m.DetailOrderModule),
      },
      {
        path: 'provider',
        data: { pageTitle: 'panysoftApp.provider.home.title' },
        loadChildren: () => import('./provider/provider.module').then(m => m.ProviderModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
