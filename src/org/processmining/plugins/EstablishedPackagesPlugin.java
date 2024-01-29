package org.processmining.plugins;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.packagemanager.PMController;
import org.processmining.contexts.uitopia.packagemanager.PMPackage;
import org.processmining.framework.boot.Boot;
import org.processmining.framework.util.HTMLToString;

//@Plugin(name = "Show Established Packages", parameterLabels = {}, returnLabels = { "Release info" }, returnTypes = { HTMLToString.class }, userAccessible = true)
public class EstablishedPackagesPlugin implements HTMLToString {

	private Set<String> allPrimaryPacks;
	private Set<String> allSecondaryPacks;
	private List<? extends PMPackage> packages;

//	@UITopiaVariant(affiliation = UITopiaVariant.EHV, author = "H.M.W. Verbeek", email = "h.m.w.verbeek@tue.nl")
//	@PluginVariant(variantLabel = "Default", requiredParameterLabels = {})
	public static HTMLToString info(final UIPluginContext context) {
		return new EstablishedPackagesPlugin();
	}

	public void addAll(Set<String> primary, Set<String> secondary) {
		PMController packageController = new PMController(Boot.Level.NONE);
		packages = packageController.getToUninstallPackages();
		Map<String, PMPackage> map = new HashMap<String, PMPackage>();
		Set<String> toDo;
		for (PMPackage pack : packages) {
			map.put(pack.getPackageName(), pack);
		}

		primary.add("ConceptDrift");
		primary.add("ContextDottedChart");
		primary.add("DottedChart");
		primary.add("KeyValue");
		primary.add("LogTimeFilter");
		primary.add("SignatureDiscovery");
		primary.add("TraceAlignment");
		primary.add("Fuzzy");
		primary.add("LogOverview");
		primary.add("PPM");
		primary.add("LogMetrics");
		primary.add("AlphaMiner");
		primary.add("ProductData");
		primary.add("ServiceTech");
		primary.add("Woflan");
		primary.add("BPMNMeasures");
		primary.add("COSAImportExport");
		primary.add("Cosimulation");
		primary.add("TSPetrinet");
		primary.add("Uma");
		primary.add("ETConformance");
		primary.add("PomPomView");
		primary.add("ProcessLogGenerator");
		primary.add("AProMore");
		primary.add("Compliance");
		primary.add("FeaturePrediction");
		primary.add("Petra");
		primary.add("StochasticPetriNets");
		primary.add("BPMNAnalysis");
		primary.add("EvolutionaryTreeMiner");
		primary.add("OperationalSupport");

		toDo = new HashSet<String>(primary);
		secondary.addAll(primary);
		while (!toDo.isEmpty()) {
			String name = toDo.iterator().next();
			toDo.remove(name);
			secondary.add(name);
			if (!map.containsKey(name)) {
				System.err.println("Unknown established package: " + name);
			} else {
				Set<String> deps = new HashSet<String>(map.get(name).getDependencies());
				deps.removeAll(secondary);
				toDo.addAll(deps);
			}
		}
		secondary.removeAll(primary);
	}

	public EstablishedPackagesPlugin() {
		allPrimaryPacks = new HashSet<String>();
		allSecondaryPacks = new HashSet<String>();
		addAll(allPrimaryPacks, allSecondaryPacks);
	}

	public String toHTMLString(boolean includeHTMLTags) {
		StringBuffer buffer = new StringBuffer();

		if (includeHTMLTags) {
			buffer.append("<html>");
		}
		buffer.append("<h1>Established Packages</h1>");

		toHTMLString("Primary Packages", allPrimaryPacks, buffer);
		toHTMLString("Secondary Packages", allSecondaryPacks, buffer);

		if (includeHTMLTags) {
			buffer.append("</html>");
		}
		return buffer.toString();
	}

	private void toHTMLString(String header, Set<String> names, StringBuffer buffer) {
		buffer.append("<h2>" + header + "</h2>");
		buffer.append("<table>");
		buffer.append("<tr><th>Package</th><th>Version</th><th>Author</th></tr>");
		for (PMPackage pack : packages) {
			if (names.contains(pack.getPackageName())) {
				buffer.append("<tr>");
				buffer.append("<td>" + pack.getPackageName() + "</td>");
				buffer.append("<td>" + pack.getVersion() + "</td>");
				buffer.append("<td>" + pack.getAuthorName() + "</td>");
				buffer.append("</tr>");
			}
		}
		buffer.append("</tr>");
		buffer.append("</table>");
	}
}
