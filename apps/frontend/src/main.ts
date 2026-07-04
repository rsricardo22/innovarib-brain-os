import { bootstrapApplication } from '@angular/platform-browser';
import { provideHttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { Component, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CommonModule } from '@angular/common';

interface Draft {
  id: string;
  objective: string;
  content: string;
  status: string;
}

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <main class="shell">
      <section class="hero">
        <p class="eyebrow">Business Agent OS · MVP nivel 2</p>
        <h1>Innovarib Brain OS</h1>
        <p>Agentes que redactan, humanos que aprueban, empresas que conservan control y auditoría.</p>
      </section>

      <section class="grid">
        <form class="card" (ngSubmit)="createDraft()">
          <h2>Crear borrador IA</h2>
          <label>Tenant <input name="tenant" [(ngModel)]="tenantId"></label>
          <label>Objetivo <textarea name="objective" [(ngModel)]="objective"></textarea></label>
          <label>Contexto <textarea name="input" [(ngModel)]="input"></textarea></label>
          <button type="submit">Generar para aprobación</button>
        </form>

        <section class="card">
          <h2>Borradores</h2>
          <button type="button" (click)="loadDrafts()">Actualizar</button>
          <article *ngFor="let draft of drafts" class="draft">
            <strong>{{ draft.objective }}</strong>
            <span>{{ draft.status }}</span>
            <p>{{ draft.content }}</p>
            <button type="button" (click)="decide(draft.id, 'approve')">Aprobar</button>
            <button type="button" (click)="decide(draft.id, 'reject')">Rechazar</button>
          </article>
        </section>
      </section>
    </main>
  `,
  styles: [`
    .shell { max-width: 1100px; margin: 0 auto; padding: 3rem 1rem; }
    .hero { margin-bottom: 2rem; }
    .eyebrow { color: #38bdf8; text-transform: uppercase; letter-spacing: .12em; font-weight: 700; }
    h1 { font-size: clamp(2.5rem, 7vw, 5rem); margin: .25rem 0; }
    .grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(320px, 1fr)); gap: 1rem; }
    label { display: grid; gap: .4rem; margin: .8rem 0; color: #cbd5e1; }
    input, textarea { border-radius: 10px; border: 1px solid #334155; padding: .75rem; background: #020617; color: #e2e8f0; }
    button { border: 0; border-radius: 999px; padding: .7rem 1rem; margin: .25rem; background: #38bdf8; color: #082f49; font-weight: 700; cursor: pointer; }
    .draft { border-top: 1px solid #263755; padding: 1rem 0; }
    .draft span { float: right; color: #facc15; }
  `]
})
class AppComponent {
  private http = inject(HttpClient);
  tenantId = 'tenant-demo';
  objective = 'Responder a un lead interesado en automatización de procesos';
  input = 'Empresa de servicios con 30 empleados. Quiere reducir trabajo manual.';
  drafts: Draft[] = [];

  createDraft(): void {
    this.http.post<Draft>('/api/drafts', {
      agentId: 'sales-assistant', objective: this.objective, input: this.input
    }, { headers: this.headers() }).subscribe(draft => this.drafts = [draft, ...this.drafts]);
  }

  loadDrafts(): void {
    this.http.get<Draft[]>('/api/drafts', { headers: this.headers() }).subscribe(drafts => this.drafts = drafts);
  }

  decide(id: string, action: 'approve' | 'reject'): void {
    this.http.post<Draft>(`/api/drafts/${id}/${action}`, { comment: 'Decisión desde MVP' }, { headers: this.headers() })
      .subscribe(() => this.loadDrafts());
  }

  private headers(): HttpHeaders {
    return new HttpHeaders({ 'X-Tenant-Id': this.tenantId });
  }
}

bootstrapApplication(AppComponent, { providers: [provideHttpClient()] }).catch(err => console.error(err));
