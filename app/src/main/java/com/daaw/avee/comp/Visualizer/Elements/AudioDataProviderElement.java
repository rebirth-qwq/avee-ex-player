package com.daaw.avee.comp.Visualizer.Elements;

import com.daaw.avee.MainActivity;
import com.daaw.avee.comp.Visualizer.CustomPropertiesList;
import com.daaw.avee.comp.Visualizer.Elements.Base.Element;
import com.daaw.avee.comp.Visualizer.Elements.Segment.ISegmentDataProvider;
import com.daaw.avee.comp.Visualizer.Elements.Segment.SegmentAudioSpectrumData;
import com.daaw.avee.comp.Visualizer.Elements.Segment.SegmentDataProviderFactory;
import com.daaw.avee.comp.Visualizer.Graphic.IRenderState;
import com.daaw.avee.comp.Visualizer.Graphic.RenderState;
import com.daaw.avee.comp.Visualizer.ICompositionDependencies;
import com.daaw.avee.comp.Visualizer.IDependencyResourceCounter;
import com.daaw.avee.vendor.glutil.graphics.glutils.FrameBuffer;
import re.ex.avee.R;

/* JADX INFO: loaded from: classes.dex */
public class AudioDataProviderElement extends Element {
    public static final String typeName = "AudioProvider";
    private ISegmentDataProvider segmentDataProvider;

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public String getElementTypeName() {
        return typeName;
    }

    public AudioDataProviderElement() {
        super(0, 0.5f, 0.5f);
        this.segmentDataProvider = null;
        setSegmentDataProvider(null);
    }

    public void setSegmentDataProvider(ISegmentDataProvider iSegmentDataProvider) {
        this.segmentDataProvider = iSegmentDataProvider;
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.Element
    protected void onApplyCustomization(CustomPropertiesList customPropertiesList) {
        ISegmentDataProvider iSegmentDataProviderCreate = SegmentDataProviderFactory.create(customPropertiesList.getChild("sampleProvider").getTypeName(SegmentAudioSpectrumData.typeName), this.segmentDataProvider);
        this.segmentDataProvider = iSegmentDataProviderCreate;
        if (iSegmentDataProviderCreate != null) {
            iSegmentDataProviderCreate.onApplyCustomization(customPropertiesList);
        }
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.Element
    protected void onReadCustomization(CustomPropertiesList customPropertiesList, IDependencyResourceCounter iDependencyResourceCounter) {
        customPropertiesList.setCustomizationName(MainActivity.getInstance().getString(R.string.element_title_bar_name_audio_provider));
        customPropertiesList.putEmptyChild("sampleProvider", SegmentDataProviderFactory.getTypeName(this.segmentDataProvider, SegmentAudioSpectrumData.typeName), "0_general", SegmentDataProviderFactory.typeNames);
        ISegmentDataProvider iSegmentDataProvider = this.segmentDataProvider;
        if (iSegmentDataProvider != null) {
            iSegmentDataProvider.onReadCustomization(customPropertiesList);
        }
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.Element
    protected void onDestroyGLResources(RenderState renderState) {
        super.onDestroyGLResources(renderState);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public boolean onCreateGLResources(RenderState renderState) {
        return super.onCreateGLResources(renderState);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public void onRender(RenderState renderState, FrameBuffer frameBuffer) {
        super.onRender(renderState, frameBuffer);
    }

    @Override // com.daaw.avee.comp.Visualizer.Elements.Base.Element
    public void onEarlyUpdate(IRenderState iRenderState, FrameBuffer frameBuffer, ICompositionDependencies iCompositionDependencies) {
        super.onEarlyUpdate(iRenderState, frameBuffer, iCompositionDependencies);
        ISegmentDataProvider iSegmentDataProvider = this.segmentDataProvider;
        if (iSegmentDataProvider != null) {
            iSegmentDataProvider.process(iRenderState, iRenderState.getRes().getVisualizationData());
            iRenderState.getRes().getMeter().setFrameDataRmsValue(this.segmentDataProvider.getRms());
            iRenderState.getRes().getMeter().setFramePeakBarIndex(this.segmentDataProvider.getPeakBarIndex());
        }
        iRenderState.getRes().getMeter().addAudioDataProviderToFrame(this.segmentDataProvider);
    }
}
