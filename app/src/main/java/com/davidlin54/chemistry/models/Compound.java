package com.davidlin54.chemistry.models;

import com.davidlin54.chemistry.exceptions.CompoundParsingException;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by David on 2016-11-14.
 */

public class Compound {
    private Map<Element, Integer> mElementMap = new HashMap<>();
    private Pattern mElementAtomsPattern = Pattern.compile("[A-z][a-z]*\\d*|\\(([A-Z][a-z]*\\d*)+\\)\\d*");
    private Pattern mElementPattern = Pattern.compile("[A-z][a-z]*");
    private Pattern mAtomsPattern = Pattern.compile("\\d+$");

    private String mCompoundString;

    public Compound(String compound) throws CompoundParsingException {
        mCompoundString = compound;
        mElementMap.putAll(parse(compound));
    }

    private Map<Element, Integer> parse(String compound) throws CompoundParsingException {
        Map<Element, Integer> map = new HashMap<>();

        int matchedLength = 0;
        Matcher matcher = mElementAtomsPattern.matcher(compound);
        while (matcher.find()) {
            String match = matcher.group();
            matchedLength += match.length();

            int atoms = 1;
            Matcher atomsMatcher = mAtomsPattern.matcher(match);
            // find number of atoms
            if (atomsMatcher.find()) {
                atoms = Integer.valueOf(atomsMatcher.group());
            }

            // on parentheses, parse the inner compound
            if (match.contains("(")) {
                Map<Element, Integer> subMap = parse(match.substring(match.indexOf("(") + 1, match.indexOf(")")));
                for (Map.Entry<Element, Integer> subMapEntry : subMap.entrySet()) {
                    Element subElement = subMapEntry.getKey();
                    int subAtoms = subMapEntry.getValue();
                    map.put(subElement, (map.get(subElement) == null ? 0 : map.get(subElement)) + subAtoms * atoms);
                }
            } else {
                Matcher elementMatcher = mElementPattern.matcher(match);
                elementMatcher.find();

                try {
                    Element element = Element.valueOf(elementMatcher.group());
                    map.put(element, (map.get(element) == null ? 0 : map.get(element)) + atoms);
                } catch (IllegalArgumentException e) {
                    throw new CompoundParsingException(CompoundParsingException.CompoundParsingExceptionType.NON_EXISTENT_ELEMENT, elementMatcher.group());
                }
            }
        }

        // if mismatched, throw exception
        if (matchedLength != compound.length()) {
            throw new CompoundParsingException(CompoundParsingException.CompoundParsingExceptionType.COMPOUND_FORMATTING, compound);
        }

        return map;
    }

    public Map<Element, Integer> getElements() {
        return mElementMap;
    }

    @Override
    public String toString() {
        return mCompoundString;
    }
}
