from trectools import TrecEval, TrecQrel, procedures

def eval(topx):
    qrels = TrecQrel("./relevance-args-v2.qrels")

    # Generates a P@10 graph with all the runs in a directory
    path_to_runs = "./runs/"
    runs = procedures.list_of_runs_from_path(path_to_runs, "*")

    for run in runs:
        print(run.get_filename())
        te = TrecEval(run, qrels)

        rbp, residuals = te.get_rbp()

        coverage = run.get_mean_coverage(qrels, topX=topx)
        print("Average number of documents judged among top %.0f: %.2f, thats about: %.2f percent." % (topx, coverage, coverage/topx*100))
        # precision = te.get_precision(depth=topx)
        # print("precision (p"+str(topx)+"): ",precision)

        ndcg = te.get_ndcg(depth=topx, removeUnjudged=False)
        print("nDCG (n="+str(topx)+" removeUnjudged=False): "+str(ndcg))

        ndcg = te.get_ndcg(depth=topx, removeUnjudged=True)
        print("nDCG (n="+str(topx)+" removeUnjudged=True): "+str(ndcg))

        print("--------\n")

    # results = procedures.evaluate_runs(runs, qrels, per_query=True)
    # p10 = procedures.extract_metric_from_results(results, "P_10")
    # fig = procedures.plot_system_rank(p10, display_metric="P@10", outfile="plot.pdf")
    # fig.savefig("plot.pdf", bbox_inches='tight', dpi=600)

if __name__ == "__main__":
    eval(5)
