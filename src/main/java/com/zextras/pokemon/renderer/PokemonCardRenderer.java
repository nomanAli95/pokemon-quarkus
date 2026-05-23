package com.zextras.pokemon.renderer;

import com.zextras.pokemon.model.PokemonOverview;
import com.zextras.pokemon.model.PokemonSprites;
import com.zextras.pokemon.dto.AbilityInfo;
import com.zextras.pokemon.dto.EvolutionStep;
import com.zextras.pokemon.dto.MoveInfo;
import com.zextras.pokemon.dto.PokemonCard;
import com.zextras.pokemon.dto.TypeMatchup;
import com.zextras.pokemon.model.PokemonEvolution;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PokemonCardRenderer {

    private PokemonCardRenderer() {}

    public static String render(PokemonCard card) {
        PokemonOverview p = card.overview();
        PokemonSprites sprites = card.sprites();

        return "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "  <meta charset=\"UTF-8\"/>\n" +
            "  <title>#" + p.id + " " + p.name + "</title>\n" +
            "  <style>\n" +
            "    * { box-sizing: border-box; margin: 0; padding: 0; }\n" +
            "    body { font-family: system-ui, sans-serif; background: #e8eaf6; padding: 24px; }\n" +
            "    .card { max-width: 680px; margin: 0 auto; background: white; border-radius: 16px; overflow: hidden; box-shadow: 0 4px 20px rgba(0,0,0,0.15); }\n" +
            "    .header { padding: 24px; text-align: center; background: linear-gradient(135deg, #667eea, #764ba2); color: white; }\n" +
            "    .dex-num { opacity: 0.75; font-size: 1em; margin-bottom: 4px; }\n" +
            "    .header h1 { font-size: 2em; text-transform: capitalize; margin-bottom: 4px; }\n" +
            "    .genus { opacity: 0.85; font-size: 0.9em; margin-bottom: 10px; }\n" +
            "    .badges { display: flex; gap: 6px; justify-content: center; flex-wrap: wrap; }\n" +
            "    .badge { border-radius: 20px; padding: 4px 14px; font-size: 0.8em; font-weight: 600; text-transform: capitalize; color: white; }\n" +
            "    .badge-legendary { background: #f9a825; color: #333; }\n" +
            "    .badge-mythical { background: #e91e8c; }\n" +
            "    .badge-baby { background: #81d4fa; color: #333; }\n" +
            "    .type-normal{background:#A8A878;color:#333} .type-fire{background:#F08030} .type-water{background:#6890F0}\n" +
            "    .type-grass{background:#78C850} .type-electric{background:#F8D030;color:#333} .type-ice{background:#98D8D8;color:#333}\n" +
            "    .type-fighting{background:#C03028} .type-poison{background:#A040A0} .type-ground{background:#E0C068;color:#333}\n" +
            "    .type-flying{background:#A890F0} .type-psychic{background:#F85888} .type-bug{background:#A8B820;color:#333}\n" +
            "    .type-rock{background:#B8A038} .type-ghost{background:#705898} .type-dragon{background:#7038F8}\n" +
            "    .type-dark{background:#705848} .type-steel{background:#B8B8D0;color:#333} .type-fairy{background:#EE99AC;color:#333}\n" +
            "    .flavor { padding: 16px 24px; background: #fafafa; border-top: 1px solid #eee; font-style: italic; color: #555; font-size: 0.9em; text-align: center; }\n" +
            "    .artwork { text-align: center; padding: 20px; background: #fafafa; }\n" +
            "    .artwork img { max-width: 220px; }\n" +
            "    .sprites { display: flex; gap: 4px; justify-content: center; padding: 10px; background: #f5f5f5; }\n" +
            "    .sprite { image-rendering: pixelated; width: 80px; }\n" +
            "    .section { padding: 16px 24px; border-top: 1px solid #eee; }\n" +
            "    .section h2 { font-size: 0.75em; text-transform: uppercase; letter-spacing: 0.08em; color: #999; margin-bottom: 12px; }\n" +
            "    .info-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 4px 24px; }\n" +
            "    .info-row { display: flex; justify-content: space-between; padding: 5px 0; font-size: 0.88em; border-bottom: 1px solid #f5f5f5; }\n" +
            "    .info-label { color: #888; }\n" +
            "    .info-value { font-weight: 600; text-transform: capitalize; text-align: right; }\n" +
            "    .stat-row { display: flex; align-items: center; gap: 10px; margin-bottom: 7px; font-size: 0.85em; }\n" +
            "    .stat-name { width: 88px; color: #888; text-align: right; flex-shrink: 0; }\n" +
            "    .stat-val { width: 30px; font-weight: 700; text-align: right; flex-shrink: 0; }\n" +
            "    .stat-bar-bg { flex: 1; background: #eee; border-radius: 4px; height: 8px; }\n" +
            "    .stat-bar { height: 8px; border-radius: 4px; }\n" +
            "    .evo-chain { display: flex; align-items: flex-start; gap: 8px; flex-wrap: wrap; justify-content: center; }\n" +
            "    .evo-entry { text-align: center; }\n" +
            "    .evo-entry a { text-decoration: none; color: #555; font-size: 0.88em; text-transform: capitalize; display: block; padding: 4px 10px; border-radius: 8px; }\n" +
            "    .evo-entry a:hover { background: #f0f0f0; }\n" +
            "    .evo-entry.current a { font-weight: 700; color: #667eea; background: #eef0ff; }\n" +
            "    .evo-arrow { display: flex; flex-direction: column; align-items: center; justify-content: center; color: #aaa; font-size: 1.1em; padding-top: 6px; }\n" +
            "    .evo-condition { font-size: 0.7em; color: #aaa; text-align: center; max-width: 80px; }\n" +
            "    .chip-list { display: flex; flex-wrap: wrap; gap: 8px; }\n" +
            "    .chip { border-radius: 20px; padding: 4px 14px; font-size: 0.82em; font-weight: 600; text-transform: capitalize; }\n" +
            "    .chip-immune { background: #212121; color: white; }\n" +
            "    .chip-quarter { background: #4caf50; color: white; }\n" +
            "    .chip-half { background: #8bc34a; color: white; }\n" +
            "    .chip-double { background: #ff9800; color: white; }\n" +
            "    .chip-quad { background: #f44336; color: white; }\n" +
            "    .ev-list { display: flex; flex-wrap: wrap; gap: 8px; }\n" +
            "    .ev-chip { background: #e8f5e9; color: #388e3c; border-radius: 20px; padding: 4px 14px; font-size: 0.85em; font-weight: 600; }\n" +
            "    .ability-list { display: flex; flex-direction: column; gap: 8px; }\n" +
            "    .ability-item { background: #f8f8f8; border-radius: 8px; padding: 8px 12px; }\n" +
            "    .ability-name { font-size: 0.88em; font-weight: 600; text-transform: capitalize; }\n" +
            "    .ability-name.hidden { color: #999; font-style: italic; }\n" +
            "    .ability-desc { font-size: 0.8em; color: #666; margin-top: 3px; }\n" +
            "    .moves-table { width: 100%; border-collapse: collapse; font-size: 0.82em; }\n" +
            "    .moves-table th { text-align: left; color: #999; font-weight: 600; padding: 4px 6px; border-bottom: 2px solid #eee; font-size: 0.75em; text-transform: uppercase; }\n" +
            "    .moves-table td { padding: 5px 6px; border-bottom: 1px solid #f5f5f5; text-transform: capitalize; }\n" +
            "    .moves-table tr:last-child td { border-bottom: none; }\n" +
            "    .move-group { font-size: 0.75em; text-transform: uppercase; letter-spacing: 0.06em; color: #aaa; padding: 8px 6px 4px; }\n" +
            "    .dc-physical { color: #C03028; font-weight: 600; }\n" +
            "    .dc-special  { color: #6890F0; font-weight: 600; }\n" +
            "    .dc-status   { color: #888; }\n" +
            "  </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "  <div class=\"card\">\n" +
            "    <div class=\"header\">\n" +
            "      <div class=\"dex-num\">#" + String.format("%03d", p.id) + "</div>\n" +
            "      <h1>" + p.name + "</h1>\n" +
            (p.genus != null ? "      <div class=\"genus\">" + p.genus + "</div>\n" : "") +
            "      <div class=\"badges\">" + typeBadges(p.type1, p.type2) + specialBadges(p) + "</div>\n" +
            "    </div>\n" +
            flavorSection(card.flavorText()) +
            artworkSection(p.officialArtworkUrl, p.name) +
            spritesSection(sprites, p.id, p.name) +
            identitySection(p) +
            physicalSection(p) +
            speciesSection(p, card.eggGroups()) +
            typeMatchupSection(card.typeMatchups()) +
            evolutionSection(card.evolutionChain(), p.id) +
            statsSection(p) +
            evSection(p) +
            abilitiesSection(card.abilities()) +
            movesSection(card.moves()) +
            "  </div>\n" +
            "</body>\n" +
            "</html>\n";
    }

    private static String typeBadges(String type1, String type2) {
        String result = typeBadge(type1);
        if (type2 != null) result += " " + typeBadge(type2);
        return result;
    }

    private static String typeBadge(String type) {
        return "<span class=\"badge type-" + type + "\">" + type + "</span>";
    }

    private static String specialBadges(PokemonOverview p) {
        StringBuilder sb = new StringBuilder();
        if (p.legendary) sb.append(" <span class=\"badge badge-legendary\">Legendary</span>");
        if (p.mythical)  sb.append(" <span class=\"badge badge-mythical\">Mythical</span>");
        if (p.baby)      sb.append(" <span class=\"badge badge-baby\">Baby</span>");
        return sb.toString();
    }

    private static String flavorSection(String text) {
        if (text == null || text.isBlank()) return "";
        return "    <div class=\"flavor\">" + text + "</div>\n";
    }

    private static String artworkSection(String url, String name) {
        if (url == null) return "";
        return "    <div class=\"artwork\"><img src=\"" + url + "\" alt=\"" + name + " official artwork\"/></div>\n";
    }

    private static String spritesSection(PokemonSprites sprites, int id, String name) {
        if (sprites == null) return "";
        StringBuilder sb = new StringBuilder();
        if (sprites.frontDefault != null)
            sb.append("<img class=\"sprite\" src=\"/api/pokemon/").append(id).append("/sprite\" alt=\"").append(name).append("\"/>");
        if (sprites.frontShiny != null)
            sb.append("<a href=\"/api/pokemon/").append(id).append("/sprite/shiny\">")
              .append("<img class=\"sprite\" src=\"/api/pokemon/").append(id).append("/sprite/shiny\" alt=\"").append(name).append(" shiny\"/>")
              .append("</a>");
        if (sprites.backDefault != null)
            sb.append("<img class=\"sprite\" src=\"/api/pokemon/").append(id).append("/sprite/back\" alt=\"").append(name).append(" back\"/>");
        if (sb.isEmpty()) return "";
        return "    <div class=\"sprites\">" + sb + "</div>\n";
    }

    private static String identitySection(PokemonOverview p) {
        return section("Identity",
            "<div class=\"info-grid\">" +
            infoRow("Generation", p.generation != null ? p.generation.replace("-", " ") : "—") +
            infoRow("Region", p.region) +
            infoRow("Color", p.color) +
            infoRow("Shape", p.shape) +
            infoRow("Habitat", p.habitat != null ? p.habitat : "—") +
            "</div>"
        );
    }

    private static String physicalSection(PokemonOverview p) {
        String height = p.height != null ? String.format("%.1f m", p.height / 10.0) : "—";
        String weight = p.weight != null ? String.format("%.1f kg", p.weight / 10.0) : "—";
        return section("Physical",
            "<div class=\"info-grid\">" +
            infoRow("Height", height) +
            infoRow("Weight", weight) +
            infoRow("Base Exp.", p.baseExperience != null ? String.valueOf(p.baseExperience) : "—") +
            "</div>"
        );
    }

    private static String speciesSection(PokemonOverview p, List<String> eggGroups) {
        String groups = eggGroups.isEmpty() ? "—" : eggGroups.stream()
            .map(g -> g.replace("-", " ").replaceAll("(\\D)(\\d)", "$1 $2"))
            .collect(Collectors.joining(", "));
        return section("Species",
            "<div class=\"info-grid\">" +
            infoRow("Growth Rate", p.growthRate) +
            infoRow("Gender", formatGender(p.genderRate)) +
            infoRow("Capture Rate", p.captureRate != null ? String.valueOf(p.captureRate) : "—") +
            infoRow("Base Happiness", p.baseHappiness != null ? String.valueOf(p.baseHappiness) : "—") +
            infoRow("Hatch Steps", p.hatchCounter != null ? "~" + ((p.hatchCounter + 1) * 255) : "—") +
            infoRow("Egg Groups", groups) +
            "</div>"
        );
    }

    private static String typeMatchupSection(List<TypeMatchup> matchups) {
        if (matchups.isEmpty()) return "";
        StringBuilder sb = new StringBuilder("<div class=\"chip-list\">");
        for (TypeMatchup tm : matchups) {
            String css = switch (tm.factor()) {
                case 0   -> "chip chip-immune";
                case 25  -> "chip chip-quarter";
                case 50  -> "chip chip-half";
                case 200 -> "chip chip-double";
                case 400 -> "chip chip-quad";
                default  -> "chip";
            };
            String label = switch (tm.factor()) {
                case 0   -> "0×";
                case 25  -> "¼×";
                case 50  -> "½×";
                case 200 -> "2×";
                case 400 -> "4×";
                default  -> tm.factor() + "";
            };
            sb.append("<span class=\"").append(css).append("\">")
              .append(tm.type()).append(" <strong>").append(label).append("</strong></span>");
        }
        sb.append("</div>");
        return section("Type Matchups", sb.toString());
    }

    private static String evolutionSection(List<EvolutionStep> chain, int currentId) {
        if (chain.isEmpty()) return "";
        StringBuilder sb = new StringBuilder("<div class=\"evo-chain\">");
        for (int i = 0; i < chain.size(); i++) {
            EvolutionStep step = chain.get(i);
            if (i > 0) {
                String condition = step.conditions() != null ? formatEvolutionCondition(step.conditions()) : "";
                sb.append("<div class=\"evo-arrow\">→<div class=\"evo-condition\">").append(condition).append("</div></div>");
            }
            boolean isCurrent = step.id() == currentId;
            sb.append("<div class=\"evo-entry").append(isCurrent ? " current" : "").append("\">")
              .append("<a href=\"/api/pokemon/").append(step.id()).append("/card/full\">")
              .append("#").append(String.format("%03d", step.id())).append("<br/>").append(step.name())
              .append("</a></div>");
        }
        sb.append("</div>");
        return section("Evolution Chain", sb.toString());
    }

    private static String statsSection(PokemonOverview p) {
        return section("Base Stats",
            statBar("HP", p.hp) +
            statBar("Attack", p.attack) +
            statBar("Defense", p.defense) +
            statBar("Sp. Attack", p.spAttack) +
            statBar("Sp. Defense", p.spDefense) +
            statBar("Speed", p.speed) +
            statBar("Total", p.baseStatTotal)
        );
    }

    private static String evSection(PokemonOverview p) {
        StringBuilder chips = new StringBuilder();
        if (p.evHp != null && p.evHp > 0)           chips.append(evChip("HP", p.evHp));
        if (p.evAttack != null && p.evAttack > 0)    chips.append(evChip("Attack", p.evAttack));
        if (p.evDefense != null && p.evDefense > 0)  chips.append(evChip("Defense", p.evDefense));
        if (p.evSpAttack != null && p.evSpAttack > 0) chips.append(evChip("Sp. Atk", p.evSpAttack));
        if (p.evSpDefense != null && p.evSpDefense > 0) chips.append(evChip("Sp. Def", p.evSpDefense));
        if (p.evSpeed != null && p.evSpeed > 0)      chips.append(evChip("Speed", p.evSpeed));
        if (chips.isEmpty()) return "";
        return section("EV Yield", "<div class=\"ev-list\">" + chips + "</div>");
    }

    private static String abilitiesSection(List<AbilityInfo> abilities) {
        if (abilities.isEmpty()) return "";
        StringBuilder sb = new StringBuilder("<div class=\"ability-list\">");
        for (AbilityInfo a : abilities) {
            String name = a.name().replace("-", " ");
            sb.append("<div class=\"ability-item\">")
              .append("<div class=\"ability-name").append(a.hidden() ? " hidden" : "").append("\">")
              .append(name).append(a.hidden() ? " (hidden)" : "").append("</div>");
            if (a.shortEffect() != null)
                sb.append("<div class=\"ability-desc\">").append(a.shortEffect()).append("</div>");
            sb.append("</div>");
        }
        sb.append("</div>");
        return section("Abilities", sb.toString());
    }

    private static String movesSection(List<MoveInfo> moves) {
        if (moves.isEmpty()) return "";

        Map<String, List<MoveInfo>> grouped = moves.stream()
            .collect(Collectors.groupingBy(MoveInfo::learnMethod, Collectors.toList()));

        List<String> order = List.of("level-up", "machine", "egg", "tutor");
        List<String> keys = new java.util.ArrayList<>(grouped.keySet());
        keys.sort(Comparator.comparingInt(k -> { int i = order.indexOf(k); return i == -1 ? 99 : i; }));

        StringBuilder sb = new StringBuilder("<table class=\"moves-table\">");
        sb.append("<thead><tr><th>Move</th><th>Type</th><th>Class</th><th>Pwr</th><th>PP</th><th>Lv</th></tr></thead><tbody>");
        for (String method : keys) {
            sb.append("<tr><td colspan=\"6\" class=\"move-group\">").append(method.replace("-", " ")).append("</td></tr>");
            for (MoveInfo m : grouped.get(method)) {
                String typeBadge = m.type() != null ? "<span class=\"badge type-" + m.type() + "\" style=\"font-size:0.75em;padding:2px 8px\">" + m.type() + "</span>" : "—";
                String dcClass = m.damageClass() != null ? "dc-" + m.damageClass() : "";
                sb.append("<tr>")
                  .append("<td>").append(m.name()).append("</td>")
                  .append("<td>").append(typeBadge).append("</td>")
                  .append("<td class=\"").append(dcClass).append("\">").append(m.damageClass() != null ? m.damageClass() : "—").append("</td>")
                  .append("<td>").append(m.power() != null ? m.power() : "—").append("</td>")
                  .append("<td>").append(m.pp() != null ? m.pp() : "—").append("</td>")
                  .append("<td>").append(m.level() > 0 ? m.level() : "—").append("</td>")
                  .append("</tr>");
            }
        }
        sb.append("</tbody></table>");
        return section("Moves", sb.toString());
    }

    private static String section(String title, String content) {
        return "    <div class=\"section\"><h2>" + title + "</h2>" + content + "</div>\n";
    }

    private static String infoRow(String label, String value) {
        String v = value != null ? value : "—";
        return "<div class=\"info-row\"><span class=\"info-label\">" + label + "</span><span class=\"info-value\">" + v + "</span></div>";
    }

    private static String statBar(String name, Integer value) {
        if (value == null) return "";
        int max = name.equals("Total") ? 800 : 255;
        int pct = (int) Math.min(100, (value / (double) max) * 100);
        String color = name.equals("Total") ? "#7c4dff"
            : value >= 120 ? "#2196F3" : value >= 90 ? "#4CAF50" : value >= 50 ? "#FF9800" : "#f44336";
        return "<div class=\"stat-row\">" +
               "<span class=\"stat-name\">" + name + "</span>" +
               "<span class=\"stat-val\">" + value + "</span>" +
               "<div class=\"stat-bar-bg\"><div class=\"stat-bar\" style=\"width:" + pct + "%;background:" + color + "\"></div></div>" +
               "</div>";
    }

    private static String evChip(String stat, int value) {
        return "<span class=\"ev-chip\">" + value + " " + stat + "</span>";
    }

    private static String formatGender(Integer genderRate) {
        if (genderRate == null) return "—";
        if (genderRate == -1) return "Genderless";
        if (genderRate == 0)  return "100% ♂";
        if (genderRate == 8)  return "100% ♀";
        double femalePct = (genderRate / 8.0) * 100;
        return String.format("%.1f%% ♀ / %.1f%% ♂", femalePct, 100 - femalePct);
    }

    private static String formatEvolutionCondition(PokemonEvolution evo) {
        if (evo == null) return "";
        if (evo.minimumLevel != null)      return "Level " + evo.minimumLevel;
        if (evo.triggerItem != null)       return "Use " + evo.triggerItem.replace("-", " ");
        if (evo.minimumHappiness != null)  return "Happiness ≥" + evo.minimumHappiness;
        if (evo.minimumAffection != null)  return "Affection ≥" + evo.minimumAffection;
        if ("trade".equals(evo.evolutionTrigger)) {
            if (evo.heldItem != null)      return "Trade w/ " + evo.heldItem.replace("-", " ");
            return "Trade";
        }
        if (evo.timeOfDay != null)         return evo.timeOfDay + " level-up";
        if (evo.evolutionTrigger != null)  return evo.evolutionTrigger.replace("-", " ");
        return "";
    }
}
