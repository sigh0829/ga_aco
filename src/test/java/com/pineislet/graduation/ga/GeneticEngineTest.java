package com.pineislet.graduation.ga;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 *  Created on 2015/5/8.
 *  @author Yasenia
 */
public class GeneticEngineTest {

    @Test
    public void testEvolution() throws Exception {

        Foo f1 = new Foo(new boolean[]{false, false, false, true, false, false, false, false});
        Foo f2 = new Foo(new boolean[]{true, false, true, false, false, false, false, false});
        Foo f3 = new Foo(new boolean[]{false, false, false, false, true, false, true, false});
        Foo f4 = new Foo(new boolean[]{false, true, false, false, false, false, false, false});

        List<Foo> list = new ArrayList<>();
        list.add(f1);
        list.add(f2);
        list.add(f3);
        list.add(f4);

        GeneticEngine<Foo> engine = new GeneticEngine<>(list);
        for (int i = 0; i < 10; i++) {
            engine.evolution();
            engine.getCurrentPopulation().forEach(foo -> {
                boolean[] gene = foo.getGene();
                for (boolean aGene : gene) {
                    System.out.print(aGene + "  ");
                }
                System.out.println();
            });
            System.out.println();
        }
    }

    static class Foo implements Individual {
        private boolean[] gene;

        public Foo(boolean[] gene) {
            this.gene = gene;
        }

        @Override
        public double calcFitness() {
            int x = 0;
            int y = 0;
            for (int i = 0; i < gene.length / 2; i++) {
                if (gene[i]) {
                    x += Math.pow(2, gene.length / 2 - i);
                }
                if (gene[gene.length / 2 + i]) {
                    y += Math.pow(2, gene.length / 2 - i);
                }
            }
            return x * x + y * y;
        }

        @Override
        public boolean[] getGene() {
            return gene;
        }

        @Override
        public Individual createIndividual(boolean[] gene) {
            return new Foo(gene);
        }
    }
}