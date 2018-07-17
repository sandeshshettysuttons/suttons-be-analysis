import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import {MailRecipientListComponent} from "./mailRecipient-list.component";

describe('MailRecipientListComponent', () => {
  let component: MailRecipientListComponent;
  let fixture: ComponentFixture<MailRecipientListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MailRecipientListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MailRecipientListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
