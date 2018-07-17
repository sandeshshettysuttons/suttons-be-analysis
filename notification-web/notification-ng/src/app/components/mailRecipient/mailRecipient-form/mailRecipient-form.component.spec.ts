import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import {MailRecipientFormComponent} from "./mailRecipient-form.component";

describe('MailRecipientFormComponent', () => {
  let component: MailRecipientFormComponent;
  let fixture: ComponentFixture<MailRecipientFormComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MailRecipientFormComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MailRecipientFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
