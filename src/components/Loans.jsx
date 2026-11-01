import {norm,fmt} from "../utils";

export function Loans({db,q,tab,setTab,status,book,reader,open}){
  const tabs=[["todos","Todos"],["Ativo","Ativos"],["Atrasado","Atrasados"],["Devolvido","Devolvidos"]];
  const list=db.loans.filter(l=>{
    const b=book(l.bookId),r=reader(l.readerId),s=status(l);
    const m=!q||norm(b.title+" "+b.author+" "+r.name).includes(norm(q));
    return m&&(tab==="todos"||s===tab);
  }).sort((a,b)=>b.date.localeCompare(a.date));
  return (<div>
    <div className="tabs">{tabs.map(([k,t])=>(<button key={k} className={tab===k?"on":""} onClick={()=>setTab(k)}>{t}</button>))}</div>
    {list.length===0?(<div className="empty">Nenhum empréstimo nesta lista.</div>):
    (<div className="lgrid">{list.map(l=>{const b=book(l.bookId),s=status(l);return (<button className="card loan" key={l.id} onClick={()=>open(l.id)}>
      <div className="lc"><div className="cover"></div><div className="bt">{b.title}</div><div className="sm">{b.author}</div></div>
      <div>
        <b>Data de empréstimo:</b>{fmt(l.date)}
        <b>{l.returned?"Devolvido em:":"Devolução prevista:"}</b>{fmt(l.returned||l.due)}
        <b>Leitor:</b>{reader(l.readerId).name}
        <b>Status: <span className={s==="Atrasado"?"warn":""}>{s==="Atrasado"?"◆ ":""}{s}</span></b>
      </div></button>)})}</div>)}
  </div>);
}

export function LoanDetail({l,book,reader,status,onReturn,back}){
  const b=book(l.bookId),r=reader(l.readerId),s=status(l);
  return (<div className="panel" style={{marginTop:20}}>
    <div style={{display:"flex",gap:24,flexWrap:"wrap"}}>
      <div style={{textAlign:"center"}}><div className="cover big"></div><div className="bt">{b.title}</div><div className="sm">{b.author}</div></div>
      <div style={{lineHeight:1.9}}>
        <div><b>Data de empréstimo:</b> {fmt(l.date)}</div>
        <div><b>Devolução prevista:</b> {fmt(l.due)}</div>
        {l.returned&&(<div><b>Devolvido em:</b> {fmt(l.returned)}</div>)}
        <div><b>Status:</b> <span className={s==="Atrasado"?"warn":""}>{s==="Atrasado"?"◆ ":""}{s}</span></div>
        <div style={{marginTop:10}}><b>Leitor:</b> {r.name}</div>
        <div><b>CPF:</b> {r.cpf}</div>
        <div><b>Telefone:</b> {r.phone}</div>
      </div>
    </div>
    <div className="row">
      {!l.returned&&(<button className="btn g" onClick={()=>onReturn(l.id)}>Marcar como devolvido</button>)}
      <button className="btn alt" onClick={back}>Voltar</button>
    </div>
  </div>);
}
