package de.axp.portfolio.vaadin.services.ui.api;

import java.util.Set;

import de.axp.framework.api.FrameworkService;

public interface UiService extends FrameworkService {

	void registerPortfolioDefinition(PortfolioDefinition definition);

	Set<PortfolioDefinition> getPortfolioDefinitions();

	PortfolioDefinition getPortfolioDefinition(String portfolioId);
}
