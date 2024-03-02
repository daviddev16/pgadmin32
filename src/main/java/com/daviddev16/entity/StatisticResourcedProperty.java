package com.daviddev16.entity;


import java.util.HashMap;
import java.util.Map;

import com.daviddev16.core.ResourcedEntityDataNode;
import com.daviddev16.core.annotation.Resourced;
import com.daviddev16.core.data.Statistic;

@Resourced(resourceIdentifier = "Statistic16px")
public class StatisticResourcedProperty implements ResourcedEntityDataNode {

	final Map<String, String> statisticDescriptionMap = new HashMap<String, String>();
	{
		/* 
		 * REFERÊNCIA: 
		 * https://www.postgresql.org/docs/current/monitoring-stats.html 
		 * */
		statisticDescriptionMap.put("seq_scan", 		   "Número de verificações sequenciais iniciadas nesta tabela");
		statisticDescriptionMap.put("seq_tup_read", 	   "Número de linhas ativas buscadas por verificações sequenciais");
		statisticDescriptionMap.put("idx_scan", 		   "Número de varreduras de índice iniciadas nesta tabela");
		statisticDescriptionMap.put("idx_tup_fetch", 	   "Número de linhas ativas buscadas por varreduras de índice");
		statisticDescriptionMap.put("n_tup_ins", 		   "Número de linhas inseridas");
		statisticDescriptionMap.put("n_tup_upd", 		   "Número de linhas atualizadas (inclui linhas atualizadas HOT)");
		statisticDescriptionMap.put("n_tup_del", 		   "Número de linhas excluídas");
		statisticDescriptionMap.put("n_tup_hot_upd", 	   "Número de linhas HOT atualizadas (ou seja, sem necessidade de atualização de índice separada)");
		statisticDescriptionMap.put("n_live_tup", 		   "Número estimado de linhas ativas");
		statisticDescriptionMap.put("n_dead_tup", 		   "Número estimado de linhas mortas");
		statisticDescriptionMap.put("n_mod_since_analyze", "Número estimado de linhas modificadas desde a última análise desta tabela");
		statisticDescriptionMap.put("last_vacuum", 	       "Última vez em que esta tabela foi aspirada manualmente (sem contar VACUUM FULL)");
		statisticDescriptionMap.put("last_autovacuum",     "Última vez que esta tabela foi limpa pelo daemon autovacuum");
		statisticDescriptionMap.put("last_analyze", 	   "Última vez em que esta tabela foi analisada manualmente");
		statisticDescriptionMap.put("last_autoanalyze",    "Última vez em que esta tabela foi analisada pelo daemon autovacuum");
		statisticDescriptionMap.put("vacuum_count",        "Número de vezes que esta tabela foi limpa manualmente (sem contar VACUUM FULL)");
		statisticDescriptionMap.put("autovacuum_count",    "Número de vezes que esta tabela foi limpa pelo daemon autovacuum");
		statisticDescriptionMap.put("analyze_count",       "Número de vezes que esta tabela foi analisada manualmente");
		statisticDescriptionMap.put("autoanalyze_count",   "Número de vezes que esta tabela foi analisada pelo daemon autovacuum");
	}

	private String statisticDescription;

	public StatisticResourcedProperty(Statistic statistic, boolean showStatisticBracketInformation) {
		if (statistic.getPropertyName() != null) {
			String statisticPrettyDescription = statisticDescriptionMap.get(statistic.getPropertyName());
			if (showStatisticBracketInformation)
				statisticDescription = String.format("<html>[<b>%s</b>] %s</html>", 
						statistic.getPropertyName(), statisticPrettyDescription);
			else
				statisticDescription = statisticPrettyDescription;
		} else {
			statisticDescription = "<unknown statistic>";
		}
	}

	@Override
	public String getNodeName() {
		return statisticDescription;
	}

	@Override
	public String getNodeIdentifier() {
		return null;
	}
}
