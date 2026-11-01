const ICONS={
  inicio:"M4 20V11l8-7 8 7v9h-5v-6H9v6z",
  livros:"M12 6c-2-1.5-5-2-9-2v14c4 0 7 .5 9 2 2-1.5 5-2 9-2V4c-4 0-7 .5-9 2zM12 6v14",
  emprestimos:"M4 6h16v14H4zM4 11h16M8 3v5M16 3v5M8 14h2M12 14h2M16 14h2M8 17h2M12 17h2",
  config:"M12 8a4 4 0 100 8 4 4 0 000-8zM12 2v3M12 19v3M2 12h3M19 12h3M5 5l2 2M17 17l2 2M5 19l2-2M17 7l2-2"};
const Icon=({n})=>(<svg viewBox="0 0 24 24" strokeLinejoin="round"><path d={ICONS[n]}/></svg>);
export default Icon;
